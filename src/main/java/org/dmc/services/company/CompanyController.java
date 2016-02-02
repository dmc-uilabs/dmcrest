package org.dmc.services.company;

import org.dmc.services.ServiceLogger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

	private final String logTag = CompanyController.class.getName();
	
    private CompanyDao CompanyDao = new CompanyDao(); 
	
    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    public Company getCompany(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "getCompany, id: " + id);
    	return CompanyDao.getCompany(id);
    }
}