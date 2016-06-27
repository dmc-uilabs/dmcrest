package org.dmc.services.resources;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.models.ResourceLabModel;
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
public class ResourceLabController {

	@Inject
	private ResourceLabService resourceLabService;


	@RequestMapping(value = "/resource/lab", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceLabModel> getAllLabs() {
		return resourceLabService.getAll();
	}
	
	@RequestMapping(value = "/resource/lab/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceLabModel getLab(@PathVariable Integer id){
		return resourceLabService.get(id);
	}

/*
	@RequestMapping(value = "/resource/lab", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceLabModel createLab(@RequestBody ResourceLabModel lab) {
		return resourceLabService.create(lab);
	}
	
	
	@RequestMapping(value = "/resource/lab/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceLabModel> deleteLab(@PathVariable Integer id) {
		return resourceLabService.remove(id);
	}*/
	

}
