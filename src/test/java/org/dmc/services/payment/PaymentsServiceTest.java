package org.dmc.services.payment;

import org.dmc.services.ApplicationTestConfig;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.OrganizationUserService;
import org.dmc.services.ServiceUsePermitService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.ServiceUsePermit;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.OrgCreation;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.PaymentStatus;
import org.dmc.services.exceptions.TooManyAttemptsException;
import org.dmc.services.organization.OrganizationService;
import org.dmc.services.payments.PaymentPlanService;
import org.dmc.services.payments.PaymentReceiptService;
import org.dmc.services.payments.PaymentService;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import org.springframework.boot.test.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestConfig.class)
public class PaymentsServiceTest {
	
	private final String testKey = "sk_test_MQ0ysMK0BD7jH4BLSivbXEwO";
	private final String goodToken = "tok_visa";
	private final String badToken = "tok_chargeDeclined";
	private static OrgCreation orgCreation;
	private static User testUser;

	@InjectMocks
	private PaymentService paymentService;
	
	@Mock
	private PaymentStatus paymentStatus;
	
	@Mock
	private UserService userService;
	
	@Mock
	private PaymentReceiptService receiptService;
	
	@Mock
	private OrganizationService orgService;
	
	@Mock
	private OrganizationUserService orgUserService;
	
	@Mock
	private PaymentPlanService payPlanService;
	
	@Mock
	private ServiceUsePermitService supService;
	
    @Before
    public void setup() throws Exception {
    	Stripe.apiKey = testKey;
        TestUserUtil.setupSecurityContext(1, "testUser");
        orgCreation = getOrgCreationObj();
        testUser = setUpTestUser();
    }

    @Test
    public void testOrgStripeSuccess() throws Exception {
    	
    	OrganizationModel orgModel = orgCreation.getOrganizationModel();
    	orgCreation.setStripeToken(goodToken);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	when(userService.canCreateOrg(1)).thenReturn(true);
    	when(receiptService.getPaymentFailureCount(testUser)).thenReturn(1);
    	when(orgService.findByUser()).thenReturn(orgModel);
    	when(orgService.save(orgModel)).thenReturn(orgModel);
    	
    	paymentStatus = paymentService.processOrganizationPayment(orgCreation);
    	assertEquals("succeeded", paymentStatus.getStatus());
    }
    
    @Test(expected = StripeException.class)
    public void testOrgStripeFailure() throws Exception {
    	
    	OrganizationModel orgModel = orgCreation.getOrganizationModel();
    	orgCreation.setStripeToken(badToken);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	when(userService.canCreateOrg(1)).thenReturn(true);
    	when(receiptService.getPaymentFailureCount(testUser)).thenReturn(1);
    	when(orgService.findByUser()).thenReturn(orgModel);
    	when(orgService.save(orgModel)).thenReturn(orgModel);
    	
    	//Should throw StripeException
    	paymentStatus = paymentService.processOrganizationPayment(orgCreation);
    }
    
    @Test
    public void testInternalPayment() throws Exception {
    	
    	int quantity = 1;
    	PaymentPlan plan = new PaymentPlan();
    	plan.setId(1);
    	plan.setPrice(500);
    	ServiceUsePermit sup = new ServiceUsePermit();
    	
    	testUser.getOrganization().setAccountBalance(1000);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	when(payPlanService.getPlan(1)).thenReturn(plan);
    	when(supService.processPermitFromPlan(plan, quantity, testUser)).thenReturn(sup);
    	
    	paymentService.processInternalPayment(plan.getId(), quantity);
    	verify(orgService).deductFunds(testUser.getOrganization(), plan.getPrice());
    }
    
    @Test
    public void testAddFundsStripeSuccess() throws Exception {
    	
    	testUser.getOrganization().setIsPaid(true);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	when(receiptService.getPaymentFailureCount(testUser)).thenReturn(1);
    	
    	paymentStatus = paymentService.addFundsToAccount(goodToken, 2000);
    	assertEquals("succeeded", paymentStatus.getStatus());
    }
    
    @Test(expected = DMCServiceException.class)
    public void testAddFundsMinimumAmount() throws Exception {
    	testUser.getOrganization().setIsPaid(true);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	when(receiptService.getPaymentFailureCount(testUser)).thenReturn(1);
    	
    	//Should throw exception due to minimum amount not met
    	try {
    		paymentStatus = paymentService.addFundsToAccount(goodToken, 1999);
    	} catch (DMCServiceException e) {
    		assertEquals(DMCError.InvalidArgument, e.getError());
    		throw e;
    	}
    }
    
    @Test(expected = DMCServiceException.class)
    public void testOrganizationNotPaid() throws Exception {
    	testUser.getOrganization().setIsPaid(false);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	when(receiptService.getPaymentFailureCount(testUser)).thenReturn(1);
    	
    	//Should throw exception due to organization not paid
    	try {
    		paymentStatus = paymentService.addFundsToAccount(goodToken, 2000);
    	} catch (DMCServiceException e) {
    		assertEquals(DMCError.OrganizationNotPaid, e.getError());
    		throw e;
    	}
    }
    
    @Test(expected = TooManyAttemptsException.class)
    public void testTooManyAttempts() throws Exception {
    	
    	OrganizationModel orgModel = orgCreation.getOrganizationModel();
    	orgCreation.setStripeToken(goodToken);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	when(userService.canCreateOrg(1)).thenReturn(true);
    	when(receiptService.getPaymentFailureCount(testUser)).thenReturn(100);
    	when(orgService.findByUser()).thenReturn(orgModel);
    	when(orgService.save(orgModel)).thenReturn(orgModel);
    	
    	//Should throw TooManyAttemptsException
    	paymentStatus = paymentService.processOrganizationPayment(orgCreation);
    }
    
    private static OrgCreation getOrgCreationObj() {
    	orgCreation = new OrgCreation();
    	OrganizationModel orgModel = new OrganizationModel();
    	orgModel.setId(1);
    	orgModel.setName("test");
    	orgCreation.setOrgModel(orgModel);
    	return orgCreation;
    }
    
    private static User setUpTestUser() {
    	testUser = new User();
    	testUser.setId(1);
    	testUser.setUsername("testUser");
    	OrganizationUser orgUser = new OrganizationUser();
    	Organization org = new Organization();
    	org.setAccountBalance(0);
    	orgUser.setOrganization(org);
    	orgUser.setUser(testUser);
    	testUser.setOrganizationUser(orgUser);
    	return testUser;
    }

}
