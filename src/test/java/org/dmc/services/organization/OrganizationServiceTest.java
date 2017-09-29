package org.dmc.services.organization;

import org.dmc.services.ApplicationTestConfig;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.DMDIIDocumentService;
import org.dmc.services.DMDIIProjectService;
import org.dmc.services.OrganizationUserService;
import org.dmc.services.ResourceGroupService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.repositories.AreaOfExpertiseRepository;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.UserRoleAssignmentRepository;
import org.dmc.services.roleassignment.UserRoleAssignmentService;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestConfig.class)
public class OrganizationServiceTest {
	
	private User testUser;
	private Organization org;
	
	@InjectMocks
	private OrganizationService orgService;
	
	@Mock
	private OrganizationRepository organizationRepository;
	
	@Mock
	private AreaOfExpertiseRepository areaOfExpertiseRepository;
	
	@Mock
	private UserRoleAssignmentRepository userRoleAssignmentRepository;

	@Spy
	private MapperFactory<Organization, OrganizationModel> mapperFactory = new MapperFactory<Organization, OrganizationModel>();

	@Mock
	private UserService userService;

	@Mock
	private OrganizationUserService organizationUserService;

	@Mock
	private UserRoleAssignmentService userRoleAssignmentService;

	@Mock
	private DMDIIProjectService dmdiiProjectService;

	@Mock
	private DMDIIDocumentService dmdiiDocumentService;

	@Mock
	private ResourceGroupService resourceGroupService;

	@Mock
	private DocumentRepository documentRepository;
	
	@Before
    public void setup() throws Exception {
		testUser = setUpTestUser();
		TestUserUtil.setupSecurityContext(1, "testUser");
		org = new Organization();
	}
	
	@Test
	public void saveNewOrganizationTest() {
		Organization orgWithId = new Organization();
		orgWithId.setId(1);
		
		when(organizationRepository.save(org)).thenReturn(orgWithId);
		when(userService.getCurrentUser()).thenReturn(testUser);
		
		orgService.save(org);
		verify(resourceGroupService).addUserResourceGroup(testUser, DocumentParentType.ORGANIZATION, orgWithId.getId(), "ADMIN");
	}
	
	@Test(expected = AccessDeniedException.class)
	public void saveOrgNotAdminTest() throws AccessDeniedException {
		org.setId(1);
		
		orgService.save(org);
		
	}
	
	@Test
	public void deductFundsTest() throws Exception {
		org.setAccountBalance(1000);
		int amount = 500;
		Integer expected = org.getAccountBalance()-amount;
		org = orgService.deductFunds(org, amount);
		assertEquals(expected, org.getAccountBalance());
	}
	
	@Test(expected = DMCServiceException.class) 
	public void insuffcientFundsTest() {
		org.setAccountBalance(999);
		int amount = 1000;
		try {
			orgService.deductFunds(org, amount);
		} catch(DMCServiceException e) {
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