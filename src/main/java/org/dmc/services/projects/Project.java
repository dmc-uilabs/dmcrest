package org.dmc.services.projects;
import org.dmc.services.sharedattributes.FeatureImage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Project {

	private int id;
	private String title;
	private String projectManager;
	private Integer projectManagerId;
	private String companyId;
	private FeatureImage featureImage;
	private String images;
	private String description;
	private long dueDate;
	private ProjectTask task;
	private ProjectDiscussion discussion;
	private ProjectService service;
	private ProjectComponent component;
	private Boolean requiresAdminApprovalToJoin;
	private Boolean isPublic;
	private Integer directoryId;

	private final String logTag = Project.class.getName();

	public static final String PUBLIC = "public";
	public static final String PRIVATE = "private";

	public Project() {
		this.id = -1;
		this.title = new String();
		this.projectManager = new String();;
		this.companyId = new String();
		this.featureImage = new FeatureImage("", "");
		this.images = new String();
		this.description = new String();
		this.dueDate = -1;
		this.task = new ProjectTask(0, id);
		this.discussion = new ProjectDiscussion(0, id);
		this.service = new ProjectService(0, id);
		this.component = new ProjectComponent(0, id);
		this.requiresAdminApprovalToJoin = true;
	}

	@JsonProperty("id")
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}

	@JsonProperty("title")
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}

	@JsonProperty("projectManager")
	public String getProjectManager(){
		return projectManager;
	}
	public void setProjectManager(String projectManager){
		this.projectManager = projectManager;
	}

	@JsonProperty("projectManagerId")
	public Integer getProjectManagerId(){
		return projectManagerId;
	}
	public void setProjectManagerId(Integer projectManagerId){
		this.projectManagerId = projectManagerId;
	}

	@JsonProperty("comapanyId")
	public String getCompanyId(){
		return companyId;
	}
	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	@JsonProperty("description")
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}

	@JsonProperty("dueDate")
	public long getDueDate() {
		return dueDate;
	}
	public void setDueDate(long dueDate) {
		this.dueDate = dueDate;
	}

	@JsonProperty("images")
	public String getImages(){
		return images;
	}
	public void setImages(String images){
		this.images = images;
	}

	@JsonProperty("featureImage")
	public FeatureImage getFeatureImage(){
		return featureImage;
	}
	public void setFeatureImage(FeatureImage featureImage){
		this.featureImage = featureImage;
	}

	@JsonProperty("tasks")
	public ProjectTask getTasks(){
		return task;
	}
	public void setTasks(ProjectTask task){
		this.task = task;
	}

	@JsonProperty("services")
	public ProjectService getServices(){
		return service;
	}
	public void setServices(ProjectService service){
		this.service = service;
	}

	@JsonProperty("discussions")
	public ProjectDiscussion getDiscussions(){
		return discussion;
	}
	public void setDiscussions(ProjectDiscussion discussion){
		this.discussion = discussion;
	}

	@JsonProperty("components")
	public ProjectComponent getComponents(){
		return component;
	}
	public void setComponents(ProjectComponent component){
		this.component = component;
	}

	@JsonProperty("requiresAdminApprovalToJoin")
	public Boolean getRequiresAdminApprovalToJoin() {
		return requiresAdminApprovalToJoin;
	}

	public void setRequiresAdminApprovalToJoin(Boolean requiresAdminApprovalToJoin) {
		this.requiresAdminApprovalToJoin = requiresAdminApprovalToJoin;
	}

	@JsonProperty("isPublic")
	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	@JsonProperty("directoryId")
	public Integer getDirectoryId() {
		return this.directoryId;
	}

	public void setDirectoryId(Integer directoryId) {
		this.directoryId = directoryId;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {}

		return null;
	}

	public static int IsPublic(String projectType) {
		if (null == projectType) return 0;
		if (projectType.toLowerCase().equals(PRIVATE)) return 0;
		if (projectType.toLowerCase().equals(PUBLIC)) return 1;
		return 0;
	}

	public static Boolean needAdminApprovalToJoin(String approvalOption) {
		if (approvalOption != null && approvalOption.equals("all")) {
			return false;
		} else {
			return true;
		}
	}
}
