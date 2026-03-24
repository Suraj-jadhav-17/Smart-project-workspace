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

import com.smartteamworkspace.main.entity.ProjectMember;
import com.smartteamworkspace.main.enums.ProjectRole;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.ProjectMemberService;

@RestController
@RequestMapping("/api/project-member")
public class ProjectMemberController {
    @Autowired
	private ProjectMemberService memberService;
    @PostMapping("/add")
    public ResponseStructure<ProjectMember> addMember(
            @RequestParam Long projectId,
            @RequestParam Long userId,
            @RequestParam ProjectRole role,
            @RequestParam Long addedById) {
    	return memberService.addMember(projectId, userId, role, addedById);
    	
    }
    @PutMapping("/role")
    public ResponseStructure<ProjectMember> updateRole(
            @RequestParam Long memberId,
            @RequestParam ProjectRole role,
            @RequestParam Long updatedById) {
    	return memberService.updateMember(updatedById, memberId, role, updatedById);
    }
    @GetMapping("/project/{projectId}")
    public ResponseStructure<List<ProjectMember>>getMembers(
            @PathVariable Long projectId) {
    	return memberService.getAllProjectMember(projectId);
    }
    @DeleteMapping("/remove")	
    public ResponseStructure<ProjectMember>  removeMember(
            @RequestParam Long projectId,
            @RequestParam Long userId,
            @RequestParam Long removedById) {
    	return memberService.removeMemberById(userId, removedById);
    }

}
