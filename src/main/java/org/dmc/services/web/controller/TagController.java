package org.dmc.services.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.TagService;
import org.dmc.services.data.models.AreaOfExpertiseModel;
import org.dmc.services.security.SecurityRoles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
public class TagController {

	@Inject
	TagService tagService;

	@RequestMapping(value = "/tags/dmdiiMember", method = RequestMethod.GET)
	public List<AreaOfExpertiseModel> getDmdiiTags() {
		return tagService.getDmdiiTags();
	}

	@RequestMapping(value = "/tags/organization", method = RequestMethod.GET)
	public List<AreaOfExpertiseModel> getOrganizationTags() {
		return tagService.getOrganizationTags();
	}

}
