package org.dmc.services.dmdiitags;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIAreaOfExpertiseModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMDIITagController {

	@Inject
	DMDIITagService dmdiiTagService;

	@RequestMapping(value = "/dmdiiTag", method = RequestMethod.GET)
	public List<DMDIIAreaOfExpertiseModel> getTags() {
		return dmdiiTagService.findAll();
	}

}
