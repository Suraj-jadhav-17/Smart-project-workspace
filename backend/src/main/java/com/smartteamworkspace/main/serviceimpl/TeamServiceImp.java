package com.smartteamworkspace.main.serviceimpl;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.smartteamworkspace.main.entity.AppUser;
import com.smartteamworkspace.main.entity.Team;
import com.smartteamworkspace.main.entity.TeamMember;
import com.smartteamworkspace.main.enums.TeamRole;
import com.smartteamworkspace.main.exception.AccessDeniedException;
import com.smartteamworkspace.main.exception.ConflictException;
import com.smartteamworkspace.main.exception.InvalidRequestException;
import com.smartteamworkspace.main.exception.ResourceNotFoundException;
import com.smartteamworkspace.main.repository.AppUserRepo;
import com.smartteamworkspace.main.repository.TeamMemberRepo;
import com.smartteamworkspace.main.repository.TeamRepo;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.TeamService;

import jakarta.transaction.Transactional;

@Service
public class TeamServiceImp implements TeamService {
   @Autowired
   private TeamRepo teamRepo;
   
   @Autowired
   private TeamMemberRepo memberRepo;
   
   @Autowired
   private AppUserRepo userRepo;
 
   private ResponseStructure<Team> createResponse(Team data,int StatusCode,String message){
	   ResponseStructure<Team> response = new ResponseStructure<>();
	   response.setData(data);
	   response.setStatusCode(StatusCode);
	   response.setMessage(message);
	   return response;
   }
   @Transactional
   @Override
   public ResponseStructure<Team> createTeam(Long createdById, String name) {
	   if (name == null || name.isBlank()) {
           throw new InvalidRequestException("Team name cannot be empty");
       }
	   
	   AppUser creator = userRepo.findById(createdById)
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));

       Team team = new Team();
       team.setName(name);

       Team savedTeam = teamRepo.save(team);
       
       TeamMember owner= new TeamMember();
       owner.setTeam(savedTeam);
       owner.setUser(creator);
       owner.setRole(TeamRole.TEAM_OWNER);
       owner.setDesignation("Team Owner");
       
       memberRepo.save(owner);
	return createResponse(savedTeam, HttpStatus.CREATED.value(), "TEAM CREATED");
   }

   @Override
   public ResponseStructure<Team> updateTeam(Long teamId, String newName, Long updatedById) {
	   Team team = teamRepo.findById(teamId)
               .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

       if (!isUserTeamOwner(teamId, updatedById)) {
           throw new AccessDeniedException("Only team owner can update team");
       }
       if (newName == null || newName.isBlank()) {
           throw new InvalidRequestException("Team name cannot be empty");
       }

       team.setName(newName);

       Team updated = teamRepo.save(team);
	return createResponse(updated, HttpStatus.OK.value(), "Team updated");
   }

   @Override
   public ResponseStructure<Team> getTeamById(Long teamId) {
	   Team team = teamRepo.findById(teamId)
               .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
	return createResponse(team, HttpStatus.OK.value(), "team fetched successfully");
   }

   @Override
   public ResponseStructure<List<Team>> getTeamsByUser(Long userId) {
	   List<TeamMember> memberships = memberRepo.findByUser_UserId(userId);

	   List<Team> teams = memberships.stream()
	            .map(TeamMember::getTeam)
	            .toList();
       ResponseStructure<List<Team>> response = new ResponseStructure<>();
       response.setStatusCode(HttpStatus.OK.value());
       response.setMessage("User teams fetched successfully");
       response.setData(teams); 
	return response;
	
   }

   @Override
   public ResponseStructure<Team> deleteTeam(Long teamId, Long deletedById) {
	   Team team = teamRepo.findById(teamId)
               .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

		       if (!isUserTeamOwner(teamId, deletedById)) {
		           throw new AccessDeniedException("Only owner can delete team");
		       }

       teamRepo.delete(team);
	return createResponse(team, HttpStatus.OK.value(), "Team deleted");
	
   }

   @Override
   public boolean isUserTeamMember(Long teamId, Long userId) {
	
	return memberRepo.existsByTeam_IdAndUser_UserId(teamId, userId);
   }

   @Override
   public boolean isUserTeamOwner(Long teamId, Long userId) {
	   Optional<TeamMember> member =
               memberRepo.findByTeam_IdAndUser_UserId(teamId, userId);

	return member.isPresent()&& member.get().getRole()==TeamRole.TEAM_OWNER;
	
   }

   @Override
   public ResponseStructure<String> leaveTeam(Long teamId, Long userId) {
	   TeamMember member = memberRepo.findByTeam_IdAndUser_UserId(teamId, userId)
               .orElseThrow(() -> new ResourceNotFoundException("User not in team"));
	   if(member.getRole() == TeamRole.TEAM_OWNER){

	        long ownerCount = memberRepo.countByTeam_IdAndRole(teamId, TeamRole.TEAM_OWNER);

	        if(ownerCount <= 1){
	            throw new ConflictException("At least one owner must remain in the team");
	        }
	    }

       memberRepo.delete(member);
       ResponseStructure<String> response = new ResponseStructure<>();
       response.setStatusCode(HttpStatus.OK.value());
       response.setMessage("User left the team");
       response.setData("Success");
	return response;
   }

   @Override
   public ResponseStructure<String> transferOwnership(Long teamId, Long currentOwnerId, Long newOwnerId) {
	   if (!isUserTeamOwner(teamId, currentOwnerId)) {
           throw new AccessDeniedException("Only current owner can transfer ownership");
       }

       TeamMember currentOwner =
               memberRepo.findByTeam_IdAndUser_UserId(teamId, currentOwnerId)
                       .orElseThrow(()->new ResourceNotFoundException("Current owner not found"));

       TeamMember newOwner =
               memberRepo.findByTeam_IdAndUser_UserId(teamId, newOwnerId)
                       .orElseThrow(() -> new ResourceNotFoundException("New owner must be team member"));

       currentOwner.setRole(TeamRole.TEAM_MEMBER);
       newOwner.setRole(TeamRole.TEAM_OWNER);

       memberRepo.save(currentOwner);
       memberRepo.save(newOwner);

       ResponseStructure<String> response = new ResponseStructure<>();
       response.setStatusCode(HttpStatus.OK.value());
       response.setMessage("Ownership transferred successfully");
       response.setData("Success");
	return response;
   }
}
