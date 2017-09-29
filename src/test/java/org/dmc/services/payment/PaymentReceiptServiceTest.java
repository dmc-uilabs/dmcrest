package org.dmc.services.payment;

import org.dmc.services.ApplicationTestConfig;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.PaymentParentType;
import org.dmc.services.data.entities.PaymentReceipt;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.PaymentReceiptModel;
import org.dmc.services.data.repositories.PaymentReceiptRepository;
import org.dmc.services.payments.PaymentReceiptService;
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
public class PaymentReceiptServiceTest {
	
	private User testUser;
	private PaymentReceipt receipt;
	
	@InjectMocks
	private PaymentReceiptService receiptService;
	
	@Spy
	private MapperFactory<PaymentReceipt, PaymentReceiptModel> mapperFactory = new MapperFactory<PaymentReceipt, PaymentReceiptModel>();
	
	@Mock
	private PaymentReceiptRepository receiptRepo;
	
	@Before
    public void setup() throws Exception {
		testUser = setUpTestUser();
		receipt = new PaymentReceipt();
	}
	
	@Test
	public void savePaymentReceiptTest() {
		receipt.setParentId(1);
		receipt.setType(PaymentParentType.ORGANIZATION);
		when(receiptRepo.save(receipt)).thenReturn(receipt);
		receipt = receiptService.savePaymentReceipt(receipt);
		assertEquals(Integer.valueOf(1), receipt.getOrgId());
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