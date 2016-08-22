package org.dmc.services.organization;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.exceptions.MissingIdException;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrganizationController {

	@Inject
	private OrganizationService organizationService;

	// TODO: page and filter results depending on requirements
	@RequestMapping(value = "/organizations", method = RequestMethod.GET)
	public List<OrganizationModel> getAllOrganizations() {
		return organizationService.findAll();
	}

	@RequestMapping(value = "/organizations/{id}", method = RequestMethod.GET)
	public OrganizationModel getOrganization(@PathVariable Integer id) {
		return organizationService.findById(id);
	}

	@RequestMapping(value = "/organizations", method = RequestMethod.POST)
	public OrganizationModel saveOrganization(@RequestBody OrganizationModel organizationModel) {
		return organizationService.save(organizationModel);
	}

	@RequestMapping(value = "/organizations/{id}", method = RequestMethod.PUT)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_ADMIN)
	public OrganizationModel updateOrganization(@RequestBody OrganizationModel organizationModel) throws MissingIdException {
		if (!PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, organizationModel.getId())) {
			throw new AccessDeniedException("403 Access denied");
		}

		if (organizationModel.getId().equals(null)) {
			throw new MissingIdException("An organization ID is required to update an organization.");
		}

		return organizationService.save(organizationModel);
	}

	@RequestMapping(value = "/organization/nonMember", method = RequestMethod.GET)
	public List<OrganizationModel> getNonDmdiiMembers() {
		return organizationService.findNonDmdiiMembers();
	}
}
