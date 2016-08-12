package org.dmc.services.organization;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.security.SecurityRoles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
public class OrganizationController {
	
	@Inject
	private OrganizationService organizationService;
	
	@RequestMapping(value = "/organization", method = RequestMethod.GET, params = "organizationId")
	public OrganizationModel getOrganization(@RequestParam("organizationId") Integer organizationId) {
		return organizationService.findOne(organizationId);
	}

	@RequestMapping(value = "/organization/nonMember", method = RequestMethod.GET)
	public List<OrganizationModel> getNonDmdiiMembers() {
		return organizationService.findNonDmdiiMembers();
	}
}
