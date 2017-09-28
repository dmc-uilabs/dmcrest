package org.dmc.services.permits;

import org.dmc.services.ApplicationTestConfig;
import org.dmc.services.ServiceUsePermitService;
import org.dmc.services.data.entities.ServiceUsePermit;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.dmc.services.data.repositories.PaymentPlanRepository;
import org.dmc.services.data.repositories.ServiceUsePermitRepository;
import org.dmc.services.services.ServiceEntityService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestConfig.class)
public class ServiceUsePermitServiceTest {
	
	@InjectMocks
	private ServiceUsePermitService supService;
	
	@Mock
	private PaymentPlanRepository payPlanRepo;
	
	@Mock
	private ServiceUsePermitRepository supRepo;
	
	@Mock
	private OrganizationUserRepository orgUserRepo;
	
	@Mock
	private ServiceEntityService servService;
	
	ServiceUsePermit sup;
	
	@Before
    public void setup() throws Exception {
		sup = new ServiceUsePermit();
		sup.setUses(1);
		
	}

}
