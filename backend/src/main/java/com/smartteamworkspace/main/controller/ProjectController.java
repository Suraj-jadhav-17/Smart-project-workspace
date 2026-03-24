package com.smartteamworkspace.main.controller;

import java.time.LocalDate;
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

import com.smartteamworkspace.main.entity.Project;
import com.smartteamworkspace.main.enums.ProjectVisibility;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
    @PostMapping("/create")
	public ResponseStructure<Project> createProject(
            @RequestParam String name,
            @RequestParam ProjectVisibility visibility,
            @RequestParam(required = false) LocalDate startDay,
            @RequestParam(required = false) LocalDate deadline,
            @RequestParam Long createdById) {
		return projectService.createProject(name, visibility, startDay, deadline, createdById);
	}
	@PutMapping("/update")
	public ResponseStructure<Project> updateProject(
            @RequestParam Long projectId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProjectVisibility visibility,
            @RequestParam(required = false) LocalDate startDay,
            @RequestParam(required = false) LocalDate deadline,
            @RequestParam Long updatedById) {
		return projectService.updateProject(projectId, name, visibility, startDay, deadline, updatedById);
	}
	@GetMapping("/get/{projectId}")
	public ResponseStructure<Project> getProjectById(@PathVariable Long projectId) {
		return projectService.findProjectById(projectId);
	}
	 @GetMapping("/team/{teamId}")
	public ResponseStructure<List<Project>> getProjectByTeam(@PathVariable Long teamId){
		return projectService.getAllProject(teamId);
	}
	 @DeleteMapping("/delete")
	 public ResponseStructure<Project> deleteProject(
	            @RequestParam Long projectId,
	            @RequestParam Long deletedById) {
		 return projectService.deleteById(projectId, deletedById);
	 }
	 @PutMapping("/add-team")
	 public ResponseStructure<Project>  addTeamToProject(
	            @RequestParam Long projectId,
	            @RequestParam Long teamId,
	            @RequestParam Long userId) {
		 return projectService.addTeamToProject(projectId, teamId, userId);
				 
	 }
	 @GetMapping("/all/{userId}")
	public ResponseStructure<List<Project>> getAllProjectByUser(@PathVariable Long userId){
		return projectService.getAllProjectByUser(userId);
	}
}
