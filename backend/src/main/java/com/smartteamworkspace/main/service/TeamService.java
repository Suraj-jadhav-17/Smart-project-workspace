package com.smartteamworkspace.main.service;

import java.util.List;

import com.smartteamworkspace.main.entity.Team;
import com.smartteamworkspace.main.response.ResponseStructure;

public interface TeamService {

	ResponseStructure<Team> createTeam(Long createdById,String name);
	ResponseStructure<Team> updateTeam(Long teamId, String newName,Long updatedById);
	ResponseStructure<Team> getTeamById(Long teamId);
	ResponseStructure<List<Team>> getTeamsByUser(Long userId );
	ResponseStructure<Team> deleteTeam(Long teamId,Long deletedById);
	boolean isUserTeamMember(Long teamId, Long userId);
	boolean isUserTeamOwner(Long teamId, Long userId);
	ResponseStructure<String> leaveTeam(Long teamId, Long userId);
	ResponseStructure<String> transferOwnership(Long teamId, Long currentOwnerId, Long newOwnerId);
}
