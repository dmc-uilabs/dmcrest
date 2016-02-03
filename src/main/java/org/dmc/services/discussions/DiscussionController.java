package org.dmc.services.discussions;

import java.util.ArrayList;

import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscussionController {

	private final String logTag = DiscussionController.class.getName();
	
	private DiscussionDao discussion = new DiscussionDao(); 
    @RequestMapping(value = "/discussions/{discussionID}", method = RequestMethod.GET)
    public Discussion getDiscussion(@PathVariable("discussionID") int discussionID) {
    	ServiceLogger.log(logTag, "In getDiscussion");
    	ServiceLogger.log(logTag, "In getDiscussion, discussionID: " + discussionID);
    	return discussion.getDiscussion(discussionID);
    }
    
    private DiscussionListDao DiscussionList = new DiscussionListDao(); 
    @RequestMapping(value = "/discussions", method = RequestMethod.GET)
    public ArrayList<Discussion> getDiscussionList() {
    	ServiceLogger.log(logTag, "In getDiscussionList");
    	//ServiceLogger.log(logTag, "In getDiscussionList, authorization: " + authorization);
    	return DiscussionList.getDiscussionList();
    }
	
	@RequestMapping(value = "/projects/{pid}/discussions", method = RequestMethod.GET)
	public ArrayList<Discussion> getDiscussionList(@PathVariable("pid") int pid){
		ServiceLogger.log(logTag, "In getDiscussionList with projectId: "+ pid);
    	//ServiceLogger.log(logTag, "In getDiscussionList, authorization: " + authorization);
    	return DiscussionList.getDiscussionList(pid);
	}
    
    
    @RequestMapping(value = "/discussions/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    
    public Id createDiscussion(@RequestBody String payload) {
    	//System.out.println("In createRole role: " + name);
    	
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return discussion.createDiscussion(payload);
    	
    	//Create role and update db through JDBC then return role using new role's id
    	
    	
    	
    }

    
    /*
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateRole(@RequestParam(value="id", defaultValue="-1") int id) {
    	System.out.println("In createRole role: " + id);
    	
    	
    	//RoleDao.createRole updates the Role in the database identified by id using the provided POST params
    	//it creates an instance of this role i.e new Role(param.id, param.name, param.title.....)
    	//this controller in turn returns this updated Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return RoleDao.updateRole(params);
    }
    */
}