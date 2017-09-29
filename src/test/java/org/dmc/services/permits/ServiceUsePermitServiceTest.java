package org.dmc.services.permits;

import org.dmc.services.ApplicationTestConfig;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceUsePermitService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.PaymentPlanType;
import org.dmc.services.data.entities.ServiceEntity;
import org.dmc.services.data.entities.ServiceUsePermit;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ServiceUsePermitModel;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.dmc.services.data.repositories.ServiceUsePermitRepository;
import org.dmc.services.payments.PaymentPlanService;
import org.dmc.services.services.ServiceEntityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestConfig.class)
public class ServiceUsePermitServiceTest {
	
	private User testUser;
	private ServiceUsePermit sup;
	private Integer serviceId;
	
	@InjectMocks
	private ServiceUsePermitService supService;
	
	@Spy
	private MapperFactory<ServiceUsePermit, ServiceUsePermitModel> mapperFactory = new MapperFactory<ServiceUsePermit, ServiceUsePermitModel>();
	
	@Mock
	private PaymentPlanService payPlanService;
	
	@Mock
	private ServiceUsePermitRepository supRepo;
	
	@Mock
	private OrganizationUserRepository orgUserRepo;
	
	@Mock
	private ServiceEntityService servService;
	
	@Mock
	private UserService userService;
	
	@Before
    public void setup() throws Exception {
		sup = new ServiceUsePermit();
		sup.setUses(1);
		testUser = setUpTestUser();
		serviceId = 1;
	}
	
	@Test
	public void testProcessUse() throws Exception {
		Integer serviceId = 1;
		ServiceUsePermitModel supModel = new ServiceUsePermitModel();
		
		when(payPlanService.hasPaymentPlan(serviceId)).thenReturn(true);
		when(userService.getCurrentUser()).thenReturn(testUser);
		when(supRepo.findByOrganizationIdAndServiceId(testUser.getOrganization().getId(), serviceId)).thenReturn(sup);
		when(supRepo.save(sup)).thenReturn(sup);
		
		supModel = supService.processServiceUse(serviceId);
		Integer expected = 0;
		assertEquals(expected, supModel.getUses());
	}
	
	@Test
	public void testRefundUse() throws Exception {
		Integer serviceId = 1;
		ServiceUsePermitModel supModel = new ServiceUsePermitModel();
		
		when(payPlanService.hasPaymentPlan(serviceId)).thenReturn(true);
		when(userService.getCurrentUser()).thenReturn(testUser);
		when(supRepo.findByOrganizationIdAndServiceId(testUser.getOrganization().getId(), serviceId)).thenReturn(sup);
		when(supRepo.save(sup)).thenReturn(sup);
		
		supModel = supService.refundUse(serviceId);
		Integer expected = 2;
		assertEquals(expected, supModel.getUses());
	}
	
	@Test
	public void testFindServicePermit() throws Exception {
		Integer orgId = testUser.getOrganization().getId();
		Integer serviceId = 1;
		ServiceEntity service = new ServiceEntity();
		service.setParent("2");
		
		when(supRepo.findByOrganizationIdAndServiceId(orgId, serviceId)).thenReturn(null);
		when(servService.getService(serviceId)).thenReturn(service);
		
		sup = supService.findServiceUsePermit(serviceId, orgId);
		verify(supRepo).findByOrganizationIdAndServiceId(orgId, Integer.valueOf(service.getParent()));
		assertEquals(null, sup);
	}
	
	@Test
	public void testCheckOrgServicePermit() throws Exception {

		when(payPlanService.hasPaymentPlan(serviceId)).thenReturn(true);
		when(userService.getCurrentUser()).thenReturn(testUser);
		when(supRepo.findByOrganizationIdAndServiceId(testUser.getOrganization().getId(), serviceId)).thenReturn(sup);
		
		Boolean canUse = supService.checkOrgServicePermit(serviceId);
		verify(userService).getCurrentUser();
		assertEquals(true, canUse);
	}
	
	@Test
	public void testCheckOrgServicePermitFailure() throws Exception {
		sup.setUses(0);

		when(payPlanService.hasPaymentPlan(serviceId)).thenReturn(true);
		when(userService.getCurrentUser()).thenReturn(testUser);
		when(supRepo.findByOrganizationIdAndServiceId(testUser.getOrganization().getId(), serviceId)).thenReturn(sup);
		
		Boolean canUse = supService.checkOrgServicePermit(serviceId);
		verify(userService).getCurrentUser();
		assertEquals(false, canUse);
	}
	
	@Test
	public void testProcessPermitFromPlan() throws Exception {
		Integer quantity = 1;
		PaymentPlan plan = new PaymentPlan();
		plan.setServiceId(serviceId);
		plan.setPlanType(PaymentPlanType.PAY_ONCE);
		plan.setUses(-1);
		
		sup = supService.processPermitFromPlan(plan, quantity, testUser);
		assertEquals(plan.getUses(), sup.getUses());
	}
	
	@Test(expected = DMCServiceException.class)
	public void testOrgAlreadyHasPermitException() throws Exception {
		Integer quantity = 1;
		PaymentPlan plan = new PaymentPlan();
		plan.setServiceId(serviceId);
		plan.setPlanType(PaymentPlanType.PAY_ONCE);
		plan.setUses(-1);
		
		sup.setUses(-1);
		when(supRepo.findByOrganizationIdAndServiceId(testUser.getOrganization().getId(), serviceId)).thenReturn(sup);
		try {
			sup = supService.processPermitFromPlan(plan, quantity, testUser);
		} catch (DMCServiceException e) {
			assertEquals(DMCError.PaymentError, e.getError());
			throw e;
		}
	}
	
	private User setUpTestUser() {
    	testUser = new User();
    	testUser.setId(1);
    	testUser.setUsername("testUser");
    	OrganizationUser orgUser = new OrganizationUser();
    	Organization org = new Organization();
    	org.setId(1);
    	orgUser.setOrganization(org);
    	orgUser.setUser(testUser);
    	testUser.setOrganizationUser(orgUser);
    	return testUser;
    }

}
