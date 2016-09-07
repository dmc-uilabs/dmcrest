package org.dmc.services.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.TagService;
import org.dmc.services.data.models.AreaOfExpertiseModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
