package org.dmc.services.organization;

import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.exceptions.MissingIdException;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class OrganizationController {

	@Inject
	private OrganizationService organizationService;

	@RequestMapping(value = "/organizations", method = RequestMethod.GET)
	public Page<OrganizationModel> getAllOrganizations(
			@RequestParam("page") Integer page,
			@RequestParam("pageSize") Integer pageSize,
			@RequestParam(value = "name", required = false) List<String> names,
			@RequestParam(value = "expertiseTags", required = false) List<Integer> expertiseTags,
			@RequestParam(value = "desiredExpertiseTags", required = false) List<Integer> desiredExpertiseTags) {
		return organizationService.filter(new PageRequest(page, pageSize, null), names, expertiseTags, desiredExpertiseTags);
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

	@RequestMapping(value = "/organizations/{id}", method = RequestMethod.DELETE)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public void delete(@PathParam("organizationId") Integer organizationId) {
		organizationService.delete(organizationId);
	}
}
