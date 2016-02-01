package org.dmc.services.taskslist;

import org.dmc.services.ServiceLogger;
import org.dmc.services.tasks.Task;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import java.util.Iterator;

@RestController
public class TaskListController {
	private final String logTag = TaskListController.class.getName();
	private TaskListDao taskSearch = new TaskListDao();
	@RequestMapping(value = "tasks", method = RequestMethod.GET)
	public ArrayList<Task> getTaskList() {
		return taskSearch.getTaskList();
	}

	@RequestMapping(value = "/projects/{projectID}/tasks", method = RequestMethod.GET)
	public ArrayList<Task> getTaskList(@PathVariable("projectID") int projectId, @RequestHeader HttpHeaders headers) {

   		Iterator<String> it = headers.keySet().iterator();


        while(it.hasNext()){
        	String theKey = (String)it.next();
		   	ServiceLogger.log(logTag, "key: " + theKey + " value: " + headers.getFirst(theKey));
       }


//		ServiceLogger.log(logTag, "UserName: " + userEPPN);
		return taskSearch.getTaskList(projectId);
	}

}