package com.smartteamworkspace.main.serviceimpl;

import java.util.List;

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
import com.smartteamworkspace.main.service.TeamMemberService;

import jakarta.transaction.Transactional;

@Service
public class TeamMemberServiceImp implements TeamMemberService{
    @Autowired
	private TeamMemberRepo memberRepo;
    
    @Autowired
    private TeamRepo teamRepo;
    
    @Autowired
    private AppUserRepo userRepo;
	
    private ResponseStructure<TeamMember> createResponse(TeamMember data,int statusCode,String massage){
    	ResponseStructure<TeamMember> response= new ResponseStructure<>();
    	response.setData(data);
    	response.setStatusCode(statusCode);
    	response.setMessage(massage);
    	return response;
    }
    @Transactional
	@Override
	public ResponseStructure<TeamMember> addTeamMember(Long teamId, Long userId, TeamRole role, String designation,
			Long AddedById) {
		 Team team = teamRepo.findById(teamId)
	                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

	     AppUser user = userRepo.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found"));  
	     TeamMember actor= memberRepo.findByTeam_IdAndUser_UserId(teamId, AddedById)
	    		                .orElseThrow(()->new AccessDeniedException("Actor is not a member of the team"));
	               if(actor.getRole()!=TeamRole.TEAM_OWNER && actor.getRole()!=TeamRole.TEAM_LEADER) {
	            	   throw new AccessDeniedException("Only team owner or leader can add members");
	               }
	               if(memberRepo.existsByTeam_IdAndUser_UserId(teamId, userId)) {
	            	   throw new ConflictException("User is already a team member");
	               }
	               if(actor.getRole()==TeamRole.TEAM_LEADER &&(role==TeamRole.TEAM_OWNER||role==TeamRole.TEAM_LEADER)) {
	            	  throw new AccessDeniedException("Leader cannot assign this role"); 
	               }
	               if(designation == null || designation.isBlank()){
	            	    throw new InvalidRequestException("Designation cannot be empty");
	            	}
	               
	           TeamMember member = new TeamMember();
	           member.setUser(user);
	           member.setTeam(team);
	           member.setRole(role);
	           member.setDesignation(designation);
	           TeamMember saved = memberRepo.save(member);
	           
		return createResponse(saved, HttpStatus.CREATED.value(),"Team member added");
	}
    @Transactional
	@Override
	public ResponseStructure<TeamMember> updateMemberRole(Long memberId, TeamRole newRole, Long updatedById) {
		 TeamMember member = memberRepo.findById(memberId)
	                .orElseThrow(() -> new ResourceNotFoundException("Team member not found"));
		 
		 if(!memberRepo.existsByTeam_IdAndUser_UserId(member.getTeam().getId(), updatedById)) {
			 throw new AccessDeniedException("User is not part of the team");
		 }
		 TeamMember actor = memberRepo
			        .findByTeam_IdAndUser_UserId(member.getTeam().getId(), updatedById)
			        .orElseThrow(() -> new ResourceNotFoundException("Actor not in team"));
		 if(newRole == null){
			    throw new InvalidRequestException("Role cannot be null");
			}
		 if(actor.getRole()!=TeamRole.TEAM_OWNER && actor.getRole()!=TeamRole.TEAM_LEADER) {
			 throw new AccessDeniedException("Only owner or leader can update roles");
		 }
		 if(actor.getRole()==TeamRole.TEAM_LEADER &&(newRole==TeamRole.TEAM_OWNER||newRole==TeamRole.TEAM_LEADER)) {
       	  throw new AccessDeniedException("Leader cannot assign this role"); 
          }
		 member.setRole(newRole);
		 TeamMember updated = memberRepo.save(member);
		 
		 
		return createResponse(updated, HttpStatus.OK.value(), "Member role updated");
	}
    @Transactional
	@Override
	public ResponseStructure<TeamMember> updateDesignation(Long memberId, String newDesignation, Long updatedById) {
		TeamMember member = memberRepo.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found"));
				 if (!memberRepo.existsByTeam_IdAndUser_UserId(member.getTeam().getId(), updatedById)) {
			            throw new AccessDeniedException("User is not part of the team");
			        }
				 TeamMember actor = memberRepo
					        .findByTeam_IdAndUser_UserId(member.getTeam().getId(), updatedById)
					        .orElseThrow(() -> new ResourceNotFoundException("Actor not in team"));
				 if(actor.getRole()!=TeamRole.TEAM_OWNER && actor.getRole()!=TeamRole.TEAM_LEADER) {
					 throw new AccessDeniedException("Only owner or leader can update designation");
				 }
				 if(newDesignation == null || newDesignation.isBlank()) {
					    throw new InvalidRequestException("Designation cannot be empty");
				 }
		     member.setDesignation(newDesignation);
		 
		 TeamMember updated = memberRepo.save(member);
		 
		return createResponse(updated, HttpStatus.OK.value(), "Designation updated");
	}
    @Transactional
	@Override
	public ResponseStructure<TeamMember> getTeamMemberById(Long memberId) {
		 TeamMember member = memberRepo.findById(memberId)
	                .orElseThrow(() -> new ResourceNotFoundException("Team member not found"));

	        
		return createResponse(member, HttpStatus.OK.value(), null);
	}

	@Override
	public ResponseStructure<List<TeamMember>> getAllTeamMembers(Long teamId) {
		Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
		List<TeamMember> members = team.getMembers();
		
		ResponseStructure<List<TeamMember>> response = new ResponseStructure<>();
		response.setData(members);
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage(members.isEmpty()?"No member found":"All members fetched");
		return response;
	}

	@Override
	public boolean isUserTeamMember(Long teamId, Long userId) {
		
		return memberRepo.existsByTeam_IdAndUser_UserId(teamId, userId);
	}

	@Override
	public ResponseStructure<TeamMember> removeTeamMember(Long teamId, Long userId,Long removedby) {
		 teamRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
		TeamMember member= memberRepo.findByTeam_IdAndUser_UserId(teamId, userId)
								.orElseThrow(()->new ResourceNotFoundException("User is not a team member"));
		TeamMember actor=memberRepo.findByTeam_IdAndUser_UserId(teamId, removedby)
							.orElseThrow(()->new AccessDeniedException("Actor is not a team member"));
				if(actor.getRole()!=TeamRole.TEAM_OWNER && actor.getRole()!=TeamRole.TEAM_LEADER) {
					throw new AccessDeniedException("Only owner or leader can remove members");
				}
				if(member.getRole()==TeamRole.TEAM_OWNER) {
					throw new AccessDeniedException("Team owner cannot be removed");
				}
				if(actor.getRole()==TeamRole.TEAM_LEADER&&member.getRole()==TeamRole.TEAM_LEADER) {
					throw new AccessDeniedException("Leader cannot remove another leader");
					
				}
				
				memberRepo.delete(member);
		return createResponse(member, HttpStatus.OK.value(), "Member removed");
	}
}
