package org.dmc.services.payment;

import org.dmc.services.ApplicationTestConfig;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.OrganizationUserService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.OrgCreation;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.PaymentStatus;
import org.dmc.services.data.repositories.PaymentReceiptRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.exceptions.TooManyAttemptsException;
import org.dmc.services.organization.OrganizationService;
import org.dmc.services.payments.PaymentReceiptService;
import org.dmc.services.payments.PaymentService;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Import;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.env.MockPropertySource;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import org.springframework.boot.test.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestConfig.class)
public class PaymentsServiceTest {
	
	private final String testKey = "sk_test_MQ0ysMK0BD7jH4BLSivbXEwO";
	private final String goodToken = "tok_visa";
	private final String badToken = "tok_chargeDeclined";

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
	
    @Before
    public void setup() throws Exception {
    	Stripe.apiKey = testKey;
        
        TestUserUtil.setupSecurityContext(1, "testUser");
    }

    @Test
    public void testSuccess() throws Exception {
    	
    	OrgCreation orgCreation = getOrgCreationObj();
    	OrganizationModel orgModel = orgCreation.getOrganizationModel();
    	
    	orgCreation.setStripeToken(goodToken);
    	
    	User testUser = new User();
    	testUser.setId(1);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	when(userService.canCreateOrg(1)).thenReturn(true);
    	when(receiptService.getPaymentFailureCount(testUser)).thenReturn(1);
    	
    	when(orgService.findByUser()).thenReturn(orgModel);
    	when(orgService.save(orgModel)).thenReturn(orgModel);
    	
    	paymentStatus = paymentService.processOrganizationPayment(orgCreation);
    	assertEquals("succeeded", paymentStatus.getStatus());
    }
    
    @Test(expected = StripeException.class)
    public void testFailure() throws Exception {
    	
    	OrgCreation orgCreation = getOrgCreationObj();
    	OrganizationModel orgModel = orgCreation.getOrganizationModel();
    	
    	orgCreation.setStripeToken(badToken);
    	
    	User testUser = new User();
    	testUser.setId(1);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	when(userService.canCreateOrg(1)).thenReturn(true);
    	when(receiptService.getPaymentFailureCount(testUser)).thenReturn(1);
    	
    	when(orgService.findByUser()).thenReturn(orgModel);
    	when(orgService.save(orgModel)).thenReturn(orgModel);
    	
    	//Should throw StripeException
    	paymentStatus = paymentService.processOrganizationPayment(orgCreation);
    }
    
    @Test(expected = TooManyAttemptsException.class)
    public void testTooManyAttempts() throws Exception {
    	OrgCreation orgCreation = getOrgCreationObj();
    	OrganizationModel orgModel = orgCreation.getOrganizationModel();
    	
    	orgCreation.setStripeToken(goodToken);
    	
    	User testUser = new User();
    	testUser.setId(1);
    	
    	when(userService.getCurrentUser()).thenReturn(testUser);
    	
    	when(userService.canCreateOrg(1)).thenReturn(true);
    	when(receiptService.getPaymentFailureCount(testUser)).thenReturn(100);
    	
    	when(orgService.findByUser()).thenReturn(orgModel);
    	when(orgService.save(orgModel)).thenReturn(orgModel);
    	
    	//Should throw TooManyAttemptsException
    	paymentStatus = paymentService.processOrganizationPayment(orgCreation);
    }
    
    private static OrgCreation getOrgCreationObj() {
    	OrgCreation orgCreation = new OrgCreation();
    	OrganizationModel orgModel = new OrganizationModel();
    	orgModel.setId(1);
    	orgModel.setName("test");
    	orgCreation.setOrgModel(orgModel);
    	return orgCreation;
    }

}
