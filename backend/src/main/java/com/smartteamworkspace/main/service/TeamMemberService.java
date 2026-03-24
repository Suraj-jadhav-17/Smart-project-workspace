package com.smartteamworkspace.main.service;

import java.util.List;

import com.smartteamworkspace.main.entity.TeamMember;
import com.smartteamworkspace.main.enums.TeamRole;
import com.smartteamworkspace.main.response.ResponseStructure;

public interface TeamMemberService {

	ResponseStructure<TeamMember> addTeamMember(Long teamId,Long userId,TeamRole role,String designation,Long AddedById);
	ResponseStructure<TeamMember> updateMemberRole(Long memberId, TeamRole newRole,Long updatedById);
	ResponseStructure<TeamMember> updateDesignation(Long memberId,String newDesignation,Long updatedById);
	ResponseStructure<TeamMember> getTeamMemberById(Long id);
	ResponseStructure<List<TeamMember>> getAllTeamMembers(Long teamId);
    boolean isUserTeamMember(Long teamId,Long userId);
	ResponseStructure<TeamMember> removeTeamMember(Long teamId,Long userId,Long removeBy);
}
