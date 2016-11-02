package org.dmc.services.projects;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dmc.services.data.entities.ProjectJoinApprovalRequest;
import org.dmc.services.data.entities.ProjectJoinApprovalRequest.ProjectJoinApprovalRequestStatus;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ProjectJoinApprovalRequestModel;
import org.dmc.services.data.repositories.ProjectJoinApprovalRequestRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.exceptions.AlreadyExistsException;
import org.dmc.services.exceptions.ArgumentNotFoundException;
import org.dmc.services.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProjectJoinApprovalRequestService {
	
	@Inject
	private ProjectJoinApprovalRequestRepository projectJoinApprovalRequestRepository;
	
	@Inject
	private ProjectDao projectDao;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private ProjectMemberDao projectMemberDao;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public List<ProjectJoinApprovalRequestModel> getProjectJoinApprovalRequests(Integer projectId) {
		Mapper<ProjectJoinApprovalRequest, ProjectJoinApprovalRequestModel> mapper = mapperFactory.mapperFor(ProjectJoinApprovalRequest.class, ProjectJoinApprovalRequestModel.class);
		return mapper.mapToModel(projectJoinApprovalRequestRepository.findByProjectId(projectId));
	}
	
	public class ProjectNotElligibleForJoinRequestException extends RuntimeException {
		public ProjectNotElligibleForJoinRequestException(String msg) {
			super(msg);
		}
	}
	
	public class ProjectJoinApprovalRequestInvalidStateException extends RuntimeException {
		public ProjectJoinApprovalRequestInvalidStateException(String msg) {
			super(msg);
		}
	}
	
	public ProjectJoinApprovalRequestModel createProjectJoinApprovalRequest(Integer projectId, String userEPPN) {
		Project project = projectDao.getProject(projectId, userEPPN);
		
		if (project == null || !project.getIsPublic() || !project.getRequiresAdminApprovalToJoin()) {
			throw new ProjectNotElligibleForJoinRequestException("This project is not elligible for join approval requests");
		}
		
		User user = userRepository.findByUsername(userEPPN);
		
		ProjectJoinApprovalRequest existingRequest = projectJoinApprovalRequestRepository.findByProjectIdAndUserId(projectId, user.getId());
		if (existingRequest != null) {
			throw new AlreadyExistsException("This ProjectJoinApprovalRequest already exists");
		}
		
		ProjectJoinApprovalRequest request = new ProjectJoinApprovalRequest();
		request.setProjectId(projectId);
		request.setUser(user);
		request.setStatus(ProjectJoinApprovalRequestStatus.PENDING);
		
		ProjectJoinApprovalRequest savedRequest = projectJoinApprovalRequestRepository.save(request);
		
		Mapper<ProjectJoinApprovalRequest, ProjectJoinApprovalRequestModel> mapper = mapperFactory.mapperFor(ProjectJoinApprovalRequest.class, ProjectJoinApprovalRequestModel.class);
		return mapper.mapToModel(savedRequest);
	}
	
	@Transactional
	public ProjectJoinApprovalRequestModel approveRequest(Integer requestId, Integer approvingUserId) throws Exception {
		ProjectJoinApprovalRequest request = projectJoinApprovalRequestRepository.findOne(requestId);
		
		if (request == null) {
			throw new ArgumentNotFoundException("Cannot find projectJoinApprovalRequest with id: " + requestId);
		}
		
		if (!request.getStatus().equals(ProjectJoinApprovalRequestStatus.PENDING)) {
			throw new ProjectJoinApprovalRequestInvalidStateException("This projectJoinApprovalRequest is not in a PENDING state");
		}
		
		if (!projectMemberDao.isUserProjectAdmin(request.getProjectId(), approvingUserId)) {
			throw new AccessDeniedException("403 access denied");
		}
		
		request.setStatus(ProjectJoinApprovalRequestStatus.APPROVED);
		ProjectJoinApprovalRequest savedRequest = projectJoinApprovalRequestRepository.save(request);
		
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		projectDao.createProjectJoinRequest(request.getProjectId().toString(), request.getUser().getId().toString(), approvingUserId, userPrincipal.getUsername());
		
		Mapper<ProjectJoinApprovalRequest, ProjectJoinApprovalRequestModel> mapper = mapperFactory.mapperFor(ProjectJoinApprovalRequest.class, ProjectJoinApprovalRequestModel.class);
		return mapper.mapToModel(savedRequest);
	}
	
	public ProjectJoinApprovalRequestModel declineRequest(Integer requestId, Integer decliningUserId) throws ArgumentNotFoundException {
		ProjectJoinApprovalRequest request = projectJoinApprovalRequestRepository.findOne(requestId);
		
		if (request == null) {
			throw new ArgumentNotFoundException("Cannot find projectJoinApprovalRequest with id: " + requestId);
		}
		
		if (!request.getStatus().equals(ProjectJoinApprovalRequestStatus.PENDING)) {
			throw new ProjectJoinApprovalRequestInvalidStateException("This projectJoinApprovalRequest is not in a PENDING state");
		}
		
		if (!projectMemberDao.isUserProjectAdmin(request.getProjectId(), decliningUserId)) {
			throw new AccessDeniedException("403 access denied");
		}
		
		request.setStatus(ProjectJoinApprovalRequestStatus.DECLINED);
		ProjectJoinApprovalRequest savedRequest = projectJoinApprovalRequestRepository.save(request);
		
		Mapper<ProjectJoinApprovalRequest, ProjectJoinApprovalRequestModel> mapper = mapperFactory.mapperFor(ProjectJoinApprovalRequest.class, ProjectJoinApprovalRequestModel.class);
		return mapper.mapToModel(savedRequest);
	}

}
