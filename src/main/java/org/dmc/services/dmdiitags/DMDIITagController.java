package org.dmc.services.dmdiitags;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.AreaOfExpertiseModel;
import org.dmc.services.security.SecurityRoles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
public class DMDIITagController {

	@Inject
	DMDIITagService dmdiiTagService;

	@RequestMapping(value = "/dmdiiTag", method = RequestMethod.GET)
	public List<AreaOfExpertiseModel> getTags() {
		return dmdiiTagService.findAll();
	}

}
