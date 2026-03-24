package com.smartteamworkspace.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartteamworkspace.main.entity.TeamMember;
import com.smartteamworkspace.main.enums.TeamRole;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.TeamMemberService;

@RestController
@RequestMapping("/api/team-members")
public class TeamMemberController {
	@Autowired
	private TeamMemberService teamMemberService;
	 @PostMapping("/add")
	public ResponseStructure<TeamMember> addTeamMember(
            @RequestParam Long teamId,
            @RequestParam Long userId,
            @RequestParam TeamRole role,
            @RequestParam String designation,
            @RequestParam Long addedById) {
		return teamMemberService.addTeamMember(teamId, userId, role, designation, addedById);
	}
	 
	 @PutMapping("/update-role")
	 public ResponseStructure<TeamMember> updateMemberRole(
	            @RequestParam Long memberId,
	            @RequestParam TeamRole newRole,
	            @RequestParam Long updatedById) {
		 return teamMemberService.updateMemberRole(memberId, newRole, updatedById);
	 }
	 
	 @PutMapping("/update-designation")
	 public ResponseStructure<TeamMember> updateDesignation(
	            @RequestParam Long memberId,
	            @RequestParam String newDesignation,
	            @RequestParam Long updatedById) {
		 return teamMemberService.updateDesignation(memberId, newDesignation, updatedById);
	 }
	 
	 @GetMapping("/get/{memberId}")
	 public ResponseStructure<TeamMember> getTeamMemberById(
	            @PathVariable Long memberId) {
		 return teamMemberService.getTeamMemberById(memberId);
	 }
	 
	 @GetMapping("/team/{teamId}")
	 public ResponseStructure<List<TeamMember>> getAllTeamMembers(
	            @PathVariable Long teamId) { 
		 return teamMemberService.getAllTeamMembers(teamId);
	 }
	    
	@DeleteMapping("/remove")
	public ResponseStructure<TeamMember>  removeTeamMember(
	        @RequestParam Long teamId,
	        @RequestParam Long userId,
	        @RequestParam Long removedBy) {
		return teamMemberService.removeTeamMember(teamId, userId, removedBy);
    }
}
