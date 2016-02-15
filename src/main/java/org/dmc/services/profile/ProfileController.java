package org.dmc.services.profile;

import java.util.ArrayList;

import org.dmc.services.ServiceLogger;
import org.dmc.services.services.specifications.Specification;
import org.dmc.services.services.specifications.SpecificationDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

	private final String logTag = ProfileController.class.getName();
	
    private ProfileDao serviceDao = new ProfileDao(); 
	
    @RequestMapping(value = "/services/{id}", method = RequestMethod.GET)
    public Profile getService(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "getService, id: " + id);
    	return serviceDao.getService(id);
    }
    
    private SpecificationDao specSearch = new SpecificationDao();
    @RequestMapping(value = "/services/{serviceID}/specifications", method = RequestMethod.GET)
    public Specification getSpecification(@PathVariable("serviceID") int serviceID) {
    	ServiceLogger.log(logTag, "In getService");
    	ServiceLogger.log(logTag, "In getService, serviceID: " + serviceID);
    	return specSearch.getSpecification(serviceID);
    }
    
    private ServiceListDao serviceListDao = new ServiceListDao(); 
    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public ArrayList<Profile> getServiceList() {
    	ServiceLogger.log(logTag, "getService ");
    	return serviceListDao.getServiceList();
    }
    
    @RequestMapping(value = "/projects/{projectId}/services", method = RequestMethod.GET)
    public ArrayList<Profile> getServiceList(@PathVariable("projectId") int projectId) {
    	ServiceLogger.log(logTag, "In getService, projectId = " + projectId);
    	return serviceListDao.getServiceList(projectId);
    }
    
    @RequestMapping(value = "/components/{componentId}/services", method = RequestMethod.GET)
    public ArrayList<Profile> getServiceByComponentList(@PathVariable("componentId") int componentId) {
    	ServiceLogger.log(logTag, "In getService, componentId = " + componentId);
    	return serviceListDao.getServiceByComponentList(componentId);
    }
    
}