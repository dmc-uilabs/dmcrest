package vehicle_forge;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;

@RestController
public class TaskController {
	
	private final String logTag = "TASK_CONTROLLER";
	private TaskDao task = new TaskDao();
	
	@RequestMapping(value = "tasks/{taskID}", method = RequestMethod.GET)
	public vehicle_forge.Task getTask(@PathVariable("taskID") String taskID) {
		return task.getTask(taskID);
	}
	
    @RequestMapping(value = "/tasks/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    @ResponseBody
    public Id createTask(@RequestBody String payload) {
    	ServiceLogger.log(logTag, "Payload: " + payload);
    	
    	return task.createTask(payload);
    }
	@RequestMapping(value = "tasks", method = RequestMethod.GET)
	public ArrayList<vehicle_forge.Task> getTaskList() {
		return task.getTaskList();
	}
	
	@RequestMapping(value = "/projects/{projectID}/tasks", method = RequestMethod.GET)
	public ArrayList<vehicle_forge.Task> getTaskList(@PathVariable("projectID") int projectId) {
		return task.getTaskList(projectId);
	}
}
