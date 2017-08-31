package org.dmc.services.organization;

import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.exceptions.MissingIdException;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		OrganizationModel organizationModel = organizationService.findById(id);

		//Check to see if the user is a member, admin, or superadmin. If not then hide the values of the following fields.
		if(!PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, id) && !PermissionEvaluationHelper.userHasRole(SecurityRoles.MEMBER, id) && !PermissionEvaluationHelper.userHasRole(SecurityRoles.SUPERADMIN, id)) {
			organizationModel.setProductionCapabilities(null);
			organizationModel.setOtherOrganizationTags(null);
			organizationModel.setIsPaid(null);
			organizationModel.setAccountBalance(null);
		}

		return organizationModel;

	}
	
	@RequestMapping(value = "/organizations/user", method = RequestMethod.GET)
	public OrganizationModel getOrganizationByUser() {
		return organizationService.findByUser();
	}

	@RequestMapping(value = "/organizations/myVPC", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getMyOrganizationVPC() {
		String myVPC = organizationService.findMyVPC();

		Map<String, String> hm = new HashMap<>();
		if (myVPC == null) {
			hm.put("errorMessage", "VPC not found." );
			return new ResponseEntity<>(hm, HttpStatus.NOT_FOUND);
		} else {
			hm.put("myVPC", myVPC);
			return ResponseEntity.ok(hm);
		}

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
	public void delete(@PathVariable("id") Integer id) {
		organizationService.delete(id);
	}
}
