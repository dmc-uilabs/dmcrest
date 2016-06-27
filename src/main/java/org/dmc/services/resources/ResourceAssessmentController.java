package org.dmc.services.resources;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIMemberEventModel;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.ResourceAssessmentModel;
import org.dmc.services.data.models.DMDIIMemberNewsModel;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceAssessmentController {

	@Inject
	private ResourceAssessmentService resourceAssessmentService;


	@RequestMapping(value = "/resource/assessment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceAssessmentModel> filter() {
		return resourceAssessmentService.getAllAssessments();
	}
	
	@RequestMapping(value = "/resource/assessment/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceAssessmentModel getAssessment(@PathVariable Integer id){
		return resourceAssessmentService.getAssessment(id);
	}

/*
	@RequestMapping(value = "/resource/assessment", method = RequestMethod.POST)
	public @ResponseBody ResourceAssessmentModel saveAssessment(@RequestBody ResourceAssessmentModel assessment) {
		return resourceAssessmentService.createAssessment(assessment);
	}
	
	
	@RequestMapping(value = "/resource/assessment/{id}", params = "limit", method = RequestMethod.DELETE)
	public List<ResourceAssessmentModel> deleteAssessment(@PathVariable Integer id) {
		return resourceAssessmentService.removeAssessment(id);
	}
	*/

}
