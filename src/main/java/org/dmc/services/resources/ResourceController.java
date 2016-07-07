package org.dmc.services.resources;

import java.util.List;
import javax.inject.Inject;

import org.dmc.services.data.models.ResourceAssessmentModel;
import org.dmc.services.data.models.ResourceCourseModel;
import org.dmc.services.data.models.ResourceJobModel;
import org.dmc.services.data.models.ResourceLabModel;
import org.dmc.services.data.models.ResourceProjectModel;
import org.dmc.services.data.models.ResourceBayModel;
import org.dmc.services.data.models.ResourceMachineModel;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

	@Inject
	private ResourceCourseService resourceCourseService;
	
	@Inject
	private ResourceAssessmentService resourceAssessmentService;

	@Inject
	private ResourceProjectService resourceProjectService;
	
	@Inject
	private ResourceJobService resourceJobService;

	@Inject
	private ResourceLabService resourceLabService;
	
	@Inject
	private ResourceBayService resourceBayService; 
	
	@Inject
	private ResourceMachineService resourceMachineService; 
	
	/*
	 * Assessments
	 */
	@RequestMapping(value = "/resource/assessment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceAssessmentModel> filter() {
		return resourceAssessmentService.getAllAssessments();
	}
	
	@RequestMapping(value = "/resource/assessment/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceAssessmentModel getAssessment(@PathVariable Integer id){
		return resourceAssessmentService.getAssessment(id);
	}


	@RequestMapping(value = "/resource/assessment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceAssessmentModel saveAssessment(@RequestBody ResourceAssessmentModel assessment) {
		return resourceAssessmentService.createAssessment(assessment);
	}
	
	@RequestMapping(value = "/resource/assessment/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResourceAssessmentModel deleteAssessment(@PathVariable Integer id) {
		return resourceAssessmentService.remove(id);
	}
	
	/*
	 * Courses
	 */
	@RequestMapping(value = "/resource/course", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceCourseModel> getAllCourses() {
		return resourceCourseService.getAll();
	}
	
	@RequestMapping(value = "/resource/course/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceCourseModel getCourse(@PathVariable Integer id){
		return resourceCourseService.get(id);
	}

	@RequestMapping(value = "/resource/course", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceCourseModel createCourse(@RequestBody ResourceCourseModel course) {
		return resourceCourseService.create(course);
	}
	
	@RequestMapping(value = "/resource/course/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResourceCourseModel deleteCourse(@PathVariable Integer id) {
		return resourceCourseService.remove(id);
	}
	

	/*
	 * Projects 
	 */
	@RequestMapping(value = "/resource/project", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceProjectModel> getAllProjects() {
		return resourceProjectService.getAll();
	}
	
	@RequestMapping(value = "/resource/project/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceProjectModel getProject(@PathVariable Integer id){
		return resourceProjectService.get(id);
	}


	@RequestMapping(value = "/resource/project", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceProjectModel createProject(@RequestBody ResourceProjectModel lab) {
		return resourceProjectService.create(lab);
	}
	
	@RequestMapping(value = "/resource/project/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResourceProjectModel deleteProject(@PathVariable Integer id) {
		return resourceProjectService.remove(id);
	}
	

	/*
	 * Jobs
	 */
	@RequestMapping(value = "/resource/job", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceJobModel> getAllJobs() {
		return resourceJobService.getAll();
	}
	
	@RequestMapping(value = "/resource/job/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceJobModel getJob(@PathVariable Integer id){
		return resourceJobService.get(id);
	}


	@RequestMapping(value = "/resource/job", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceJobModel createJob(@RequestBody ResourceJobModel Job) {
		return resourceJobService.create(Job);
	}
	
	@RequestMapping(value = "/resource/job/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResourceJobModel deleteJob(@PathVariable Integer id) {
		return resourceJobService.remove(id);
	}


	/*
	 * Labs
	 */
	@RequestMapping(value = "/resource/lab", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceLabModel> getAllLabs() {
		return resourceLabService.getAll();
	}
	
	@RequestMapping(value = "/resource/lab/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceLabModel getLab(@PathVariable Integer id){
		return resourceLabService.get(id);
	}

	@RequestMapping(value = "/resource/lab", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceLabModel createLab(@RequestBody ResourceLabModel lab) {
		return resourceLabService.create(lab);
	}
	
	@RequestMapping(value = "/resource/lab/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResourceLabModel deleteLab(@PathVariable Integer id) {
		return resourceLabService.remove(id);
	}
	
	
	/*
	 * Bays
	 */
	@RequestMapping(value = "/resource/bay", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResourceBayModel> getAllBays() {
		return resourceBayService.getAll();
	}
	
	@RequestMapping(value = "/resource/bay/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResourceBayModel getBay(@PathVariable Integer id){
		return resourceBayService.get(id);
	}
	
	@RequestMapping(value = "/resource/bay", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceBayModel createBay(@RequestBody ResourceBayModel bay) {
		return resourceBayService.create(bay);
	}
	
	@RequestMapping(value = "/resource/bay/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResourceBayModel deleteBay(@PathVariable Integer id) {
		return resourceBayService.remove(id);
	}
	
	
	/*
	 * Machines
	 */
	
	@RequestMapping(value = "/resource/machine/{bayId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody List<ResourceMachineModel> getMachine(@PathVariable Integer bayId){
		return resourceMachineService.getAllMachines(bayId);
	}


	@RequestMapping(value = "/resource/machine", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResourceMachineModel createBayMachine(@RequestBody ResourceMachineModel machine) {
		return resourceMachineService.createMachine(machine);
	}
	
	
	@RequestMapping(value = "/resource/machine/{bayId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer deleteAllMachines(@PathVariable Integer bayId) {
		return resourceMachineService.removeAllMachines(bayId);
	}
	
	@RequestMapping(value = "/resource/machine/{bayId}/{machineId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResourceMachineModel deleteMachine(@PathVariable Integer bayId, @PathVariable Integer machineId) {
		return resourceMachineService.removeMachine(bayId, machineId);
	}
	
	
	
	

}
