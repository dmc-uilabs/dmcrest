package org.dmc.services.dmdiitype;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.security.SecurityRoles;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_MEMBER)
public class DMDIITypeController {

	@Inject
	private DMDIITypeService dmdiiTypeService;
	
	@RequestMapping(value = "/dmdiiType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<DMDIIType> findAll() {
		return dmdiiTypeService.findAll();
	}
	
}
