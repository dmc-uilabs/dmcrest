package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.ProjectJoinApprovalRequest;

public interface ProjectJoinApprovalRequestRepository extends BaseRepository<ProjectJoinApprovalRequest, Integer> {

	List<ProjectJoinApprovalRequest> findByProjectId(Integer projectId);
	
	ProjectJoinApprovalRequest findByProjectIdAndUserId(Integer projectId, Integer userId);
	
}
