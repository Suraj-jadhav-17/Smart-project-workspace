package com.smartteamworkspace.main.service;

import java.util.List;

import com.smartteamworkspace.main.entity.ProjectMember;
import com.smartteamworkspace.main.enums.ProjectRole;
import com.smartteamworkspace.main.response.ResponseStructure;

public interface ProjectMemberService {

	ResponseStructure<ProjectMember> addMember(Long projectId,Long userId,ProjectRole role ,Long addedbyid);
	ResponseStructure<ProjectMember> updateMember(Long projectId,Long memberId,ProjectRole newRole,Long updatedById);
	ResponseStructure<List<ProjectMember>> getAllProjectMember(Long projectId);
	ResponseStructure<ProjectMember> removeMemberById(Long id,Long removedById);
	boolean isUserProjectMember(Long projectId,Long userId);
	
}
