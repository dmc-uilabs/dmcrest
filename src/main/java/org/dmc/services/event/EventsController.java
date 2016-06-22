package org.dmc.services.event;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.Company;
import org.dmc.services.company.CompanyController;
import org.dmc.services.company.CompanyDao;
import org.dmc.services.company.CompanyVideo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.http.HTTPException;

import static org.springframework.http.MediaType.*;

//@RequestMapping(value = "/events", produces = {APPLICATION_JSON_VALUE})
//@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
@RestController


public class EventsController
{

	private final String logTag = EventsController.class.getName();
	private EventsDao eventsDao = new EventsDao();
	
	
	//GET
	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = { "application/json"})
	public ResponseEntity getEvent(@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "order", required = false, defaultValue = "DESC") String order,
			@RequestParam(value = "sort", required = false, defaultValue = "id") String sort)
	{
		ServiceLogger.log(logTag, "getEvents");
		int statusCode = HttpStatus.OK.value();
		ArrayList<CommunityEvent> events = new ArrayList<CommunityEvent>();

		try
		{
	        events = eventsDao.getEvents();
	        return new ResponseEntity<ArrayList<CommunityEvent>>(events, HttpStatus.valueOf(statusCode));
		}
		catch (DMCServiceException e)
		{
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}

	}
	
	//POST
	@RequestMapping(value = "/events", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public ResponseEntity createEvent(@RequestBody CommunityEvent event)
	{	
		ServiceLogger.log(logTag, "createCommunityEvent");
		int statusCode = HttpStatus.OK.value();
		Id id = null;
		try
		{
			id = eventsDao.createCommunityEvent(event);
		}
		
		catch(DMCServiceException e)
		{
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
	    }
		
		return new ResponseEntity<Id>(id, HttpStatus.valueOf(statusCode));		
	}
	/*
	//Method not needed and does not make sense to have. If needed can be added in. Patch method tested and works.
	//PATCH
	@RequestMapping(value = "/events/{id}", method = RequestMethod.PATCH, produces = { "application/json"})
	public ResponseEntity updateEvent(@PathVariable("id") int id, @RequestBody CommunityEvent event)
	{
		ServiceLogger.log(logTag, "updateEvent" + id);
		CommunityEvent returnEvent = new CommunityEvent(); 
		
		int statusCode = HttpStatus.OK.value();
		
		try
		{
			returnEvent = eventsDao.updateEvent(id, event);
			return new ResponseEntity<CommunityEvent>(returnEvent, HttpStatus.valueOf(statusCode));
		}
		catch(DMCServiceException e)
		{
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}  
	}
	*/

	//DELETE
	@RequestMapping(value = "/events/{id}", method = RequestMethod.DELETE, produces = { "application/json" })
	@ResponseBody
	public ResponseEntity deleteEvent(@PathVariable("id") int id)
	{	
		ServiceLogger.log(logTag, "deleteCommunityEvent");
		
		int statusCode = HttpStatus.OK.value();
		Id returnId = null;
		
		try
		{
			returnId = eventsDao.deleteCommunityEvent(id);			
		}
		
		catch(DMCServiceException e)
		{
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
	    }
		
		return new ResponseEntity<Id>(returnId, HttpStatus.valueOf(statusCode));		
	}
}

