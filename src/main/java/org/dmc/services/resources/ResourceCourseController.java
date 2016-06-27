package org.dmc.services.resources;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.models.ResourceCourseModel;
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
public class ResourceCourseController {

	@Inject
	private ResourceCourseService resourceCourseService;


	@RequestMapping(value = "/resource/course", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceCourseModel> getAllCourses() {
		return resourceCourseService.getAll();
	}
	
	@RequestMapping(value = "/resource/course/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceCourseModel getCourse(@PathVariable Integer id){
		return resourceCourseService.get(id);
	}

/*
	@RequestMapping(value = "/resource/course", method = RequestMethod.POST)
	public @ResponseBody ResourceCourseModel createCourse(@RequestBody ResourceCourseModel course) {
		return resourceAssessmentService.create(course);
	}
	
	
	@RequestMapping(value = "/resource/course/{id}", params = "limit", method = RequestMethod.DELETE)
	public List<ResourceCourseModel> deleteCourse(@PathVariable Integer id) {
		return resourceAssessmentService.remove(id);
	}
	
	*/
}
