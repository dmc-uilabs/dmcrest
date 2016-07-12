package org.dmc.services;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIQuickLinkModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMDIIQuickLinkController {
	
	private final String logTag = DMDIIQuickLinkController.class.getName();
	
	@Inject
	DMDIIQuickLinkService dmdiiQuickLinkService;
	
	@RequestMapping(value = "/dmdiiquicklink/{id}", method = RequestMethod.GET)
	public DMDIIQuickLinkModel getDMDIIQuickLinkById (@PathVariable("id") Integer id) throws DMCServiceException {
		ServiceLogger.log(logTag, "In getDMDIIQuickLinkById: " + id);
		
		return dmdiiQuickLinkService.findOne(id);
	}
	
	@RequestMapping(value = "/dmdiiquicklink", method = RequestMethod.POST)
	public DMDIIQuickLinkModel postDMDIIQuickLink (@RequestBody DMDIIQuickLinkModel link) throws DMCServiceException {
		ServiceLogger.log(logTag, "postDMDIIQuickLink");
		return dmdiiQuickLinkService.save(link);
	}
	
	@RequestMapping(value = "/dmdiiquicklink", params = "limit", method = RequestMethod.GET)
	public List<DMDIIQuickLinkModel> getDMDIIQuickLinks (@RequestParam("limit") Integer limit) throws DMCServiceException {
		return dmdiiQuickLinkService.getDMDIIQuickLinks(limit);
	}

}
