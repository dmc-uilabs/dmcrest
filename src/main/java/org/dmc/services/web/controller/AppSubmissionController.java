package org.dmc.services.web.controller;

import java.util.Set;

import javax.inject.Inject;

import org.dmc.services.AppSubmissionService;
import org.dmc.services.data.models.AppSubmissionModel;
import org.dmc.services.data.models.ApplicationTagModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppSubmissionController {
	
	@Inject
	private AppSubmissionService appSubmissionService;
	
	@RequestMapping(value = "/appSubmission/{appSubmissionId}", method = RequestMethod.GET)
	public AppSubmissionModel findOne(@PathVariable("appSubmissionId") Integer appSubmissionId) {
		return appSubmissionService.findOne(appSubmissionId);
	}

	@RequestMapping(value = "/appSubmission", method = RequestMethod.POST)
	public AppSubmissionModel save(@RequestBody AppSubmissionModel appSubmissionModel) {
		return appSubmissionService.save(appSubmissionModel);
	}
	
	@RequestMapping(value = "/applicationTag", method = RequestMethod.GET)
	public Set<ApplicationTagModel> getApplicationTags() {
		return appSubmissionService.getApplicationTags();
	}
	
	@RequestMapping(value = "/appSubmission/appName", method = RequestMethod.GET)
	public Set<String> getApplicationSubmissionNames() {
		return appSubmissionService.getAppNames();
	}
}
