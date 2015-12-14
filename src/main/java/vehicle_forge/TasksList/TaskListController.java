package vehicle_forge;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;


@RestController
public class TaskListController {
	private final String logTag = TaskListController.class.getName();
	private TaskListDao taskSearch = new TaskListDao();
	@RequestMapping(value = "tasks", method = RequestMethod.GET)
	public ArrayList<vehicle_forge.Task> getTaskList() {
		return taskSearch.getTaskList();
	}
	
}