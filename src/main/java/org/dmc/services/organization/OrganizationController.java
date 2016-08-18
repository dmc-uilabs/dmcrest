package org.dmc.services.organization;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.security.SecurityRoles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
public class OrganizationController {

	@Inject
	private OrganizationService organizationService;

	@RequestMapping(value = "/organizations/{id}", method = RequestMethod.GET)
	public OrganizationModel getOrganization(@PathVariable Integer id) {
		return organizationService.findById(id);
	}

	// TODO: page and filter results depending on requirements
	@RequestMapping(value = "/organizations", method = RequestMethod.GET)
	public List<OrganizationModel> getAllOrganizations() {
		return organizationService.findAll();
	}

	@RequestMapping(value = "/organizations", method = RequestMethod.POST)
	public OrganizationModel saveOrganization(@RequestBody OrganizationModel organizationModel) {
		return organizationService.save(organizationModel);
	}

	@RequestMapping(value = "/organizations/{id}", method = RequestMethod.PUT)
	public OrganizationModel updateOrganization(@RequestBody OrganizationModel organizationModel) {
		return organizationService.save(organizationModel);
	}

	@RequestMapping(value = "/organization/nonMember", method = RequestMethod.GET)
	public List<OrganizationModel> getNonDmdiiMembers() {
		return organizationService.findNonDmdiiMembers();
	}
}
