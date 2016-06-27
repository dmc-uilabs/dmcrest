package org.dmc.services.resources;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.models.ResourceJobModel;
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
public class ResourceJobController {

	@Inject
	private ResourceJobService resourceJobService;


	@RequestMapping(value = "/resource/job", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceJobModel> getAllJobs() {
		return resourceJobService.getAll();
	}
	
	@RequestMapping(value = "/resource/job/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceJobModel getJob(@PathVariable Integer id){
		return resourceJobService.get(id);
	}
/*

	@RequestMapping(value = "/resource/job", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceJobModel createJob(@RequestBody ResourceJobModel Job) {
		return resourceJobService.create(Job);
	}
	
	
	@RequestMapping(value = "/resource/job/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceJobModel> deleteJob(@PathVariable Integer id) {
		return resourceJobService.remove(id);
	}
	*/

}
