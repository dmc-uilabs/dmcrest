package org.dmc.services.company;

import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

	private final String logTag = CompanyController.class.getName();
	
    private CompanyDao companyDao = new CompanyDao(); 
	
    @RequestMapping(value = "/companies/{id}", method = RequestMethod.GET)
    public Company getCompany(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "getCompany, id: " + id);
    	return  companyDao.getCompany(id);
    }
    
    @RequestMapping(value = "/companies/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    @ResponseBody
    public Id createCompany(@RequestBody String payload) {
    	ServiceLogger.log(logTag, "CreateCompany, Payload: " + payload);	
    	return companyDao.createCompany(payload);
    }

    @RequestMapping(value = "/companies/{id}/update", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    @ResponseBody
    public Id updateCompany(@PathVariable("id") int id, @RequestBody String payload) {
    	ServiceLogger.log(logTag, "UpdateCompany, ID: " + id + " Payload: " + payload);	
    	return companyDao.updateCompany(id, payload);
    }
    @RequestMapping(value = "/companies/{id}/delete", method = RequestMethod.GET)
    public Id deleteCompany(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "deleteCompany, id: " + id);
    	return  companyDao.deleteCompany(id);
    }
}