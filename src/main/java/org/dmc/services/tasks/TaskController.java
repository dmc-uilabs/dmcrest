package org.dmc.services.tasks;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class TaskController {

    private static final String LOGTAG = TaskController.class.getName();

    private TaskDao task = new TaskDao();

    @RequestMapping(value = "/tasks/{taskID}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTask(@PathVariable("taskID") String taskID,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(LOGTAG, "UserName: " + userEPPN);
        try {
            return new ResponseEntity<Task>(task.getTask(taskID, userEPPN), OK);
        } catch (DMCServiceException e) {
            return new ResponseEntity<String>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/tasks/{taskID}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> tasksTaskIDDelete(@PathVariable("taskID") String taskID,
											   @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(LOGTAG, "delete /tasks/{taskID} called by UserName: " + userEPPN + " for task " + taskID);
		try {
			Id id = task.deleteTask(taskID, userEPPN);
			if(taskID.equals(Integer.toString(id.getId()))) {
				ServiceLogger.log(LOGTAG, "task " + taskID + " deleted");
				return new ResponseEntity<Void>(OK);
			}
			ServiceLogger.log(LOGTAG, "task " + taskID + " delete error " + id.getId());
			return new ResponseEntity<Void>(INTERNAL_SERVER_ERROR);
		} catch (DMCServiceException e) {
			ServiceLogger.log(LOGTAG, "task " + taskID + " delete error with message " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), INTERNAL_SERVER_ERROR);
		}
    }

    @RequestMapping(value = "/tasks/create", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTask(@RequestBody TaskToCreate payload,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(LOGTAG, "Payload: " + payload);

        try {
            final Id id = task.createTask(payload, userEPPN);
            return new ResponseEntity<Id>(id, OK);
        } catch (DMCServiceException e) {
            return new ResponseEntity<String>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/tasks", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTaskList(@RequestParam(value = "_order", required = false) String order,
            @RequestParam(value = "_sort", required = false) String sort,
            @RequestParam(value = "_start", required = false) Integer start,
            @RequestParam(value = "_limit", required = false) Integer limit,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        try {
            return new ResponseEntity<ArrayList<Task>>(task.getTaskList(), OK);
        } catch (DMCServiceException e) {
            return new ResponseEntity<String>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/projects/{projectID}/tasks", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getTaskList(@PathVariable("projectID") int projectId,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(LOGTAG, "UserName: " + userEPPN);
        try {
            return new ResponseEntity<ArrayList<Task>>(task.getTaskList(projectId), OK);
        } catch (DMCServiceException e) {
            return new ResponseEntity<String>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }
}
