package org.dmc.services.resources;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.models.ResourceProjectModel;
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
public class ResourceProjectController {

	@Inject
	private ResourceProjectService resourceProjectService;


	@RequestMapping(value = "/resource/lab", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceProjectModel> getAllProjects() {
		return resourceProjectService.getAll();
	}
	
	@RequestMapping(value = "/resource/lab/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceProjectModel getProject(@PathVariable Integer id){
		return resourceProjectService.get(id);
	}

/*
	@RequestMapping(value = "/resource/lab", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceProjectModel createProject(@RequestBody ResourceProjectModel lab) {
		return resourceProjectService.create(lab);
	}
	
	
	@RequestMapping(value = "/resource/lab/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceProjectModel> deleteProject(@PathVariable Integer id) {
		return resourceProjectService.remove(id);
	}*/
	

}
