package vehicle_forge;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

	private final String logTag = ServiceController.class.getName();
	
    private ServiceDao serviceDao = new ServiceDao(); 
	
    @RequestMapping(value = "/services/{id}", method = RequestMethod.GET)
    public Service getService(@PathVariable("id") int id) {
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
    public ArrayList<vehicle_forge.Service> getServiceList() {
    	ServiceLogger.log(logTag, "getService ");
    	return serviceListDao.getServiceList();
    }
    
    @RequestMapping(value = "/projects/{projectId}/services", method = RequestMethod.GET)
    public ArrayList<vehicle_forge.Service> getServiceList(@PathVariable("projectId") int projectId) {
    	ServiceLogger.log(logTag, "In getService, projectId = " + projectId);
    	return serviceListDao.getServiceList(projectId);
    }
    
    @RequestMapping(value = "/components/{componentId}/services", method = RequestMethod.GET)
    public ArrayList<vehicle_forge.Service> getServiceByComponentList(@PathVariable("componentId") int componentId) {
    	ServiceLogger.log(logTag, "In getService, componentId = " + componentId);
    	return serviceListDao.getServiceByComponentList(componentId);
    }
    
}