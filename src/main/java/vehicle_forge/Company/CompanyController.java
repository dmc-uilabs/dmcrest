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
public class CompanyController {

	private final String logTag = CompanyController.class.getName();
	
    private CompanyDao CompanyDao = new CompanyDao(); 
	
    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    public Company getCompany(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "getCompany, id: " + id);
    	return CompanyDao.getCompany(id);
    }
}