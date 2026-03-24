package com.smartteamworkspace.main.service;

import java.time.LocalDate;
import java.util.List;

import com.smartteamworkspace.main.entity.Project;
import com.smartteamworkspace.main.enums.ProjectStatus;
import com.smartteamworkspace.main.enums.ProjectVisibility;
import com.smartteamworkspace.main.response.ResponseStructure;

public interface ProjectService {

	ResponseStructure<Project> createProject(String name,ProjectVisibility visibility,LocalDate startDay,LocalDate deadline,Long createdById);
	
	ResponseStructure<Project> updateProject(Long projectId,String name,ProjectVisibility visibility,LocalDate startDay,LocalDate deadLine,Long updatedByID);
	
	ResponseStructure<List<Project>> getAllProject(Long teamId);
	
	ResponseStructure<Project> findProjectById(Long id);
	
	ResponseStructure<Project> deleteById(Long projectID,Long deletedById);
	
	ResponseStructure<Project> changeProjectStatus(Long projectId, ProjectStatus newStatus, Long updatedById);
	
	ResponseStructure<Project> addTeamToProject(Long projectId,Long TeamId,Long userId);
	
	ResponseStructure<List<Project> >getAllProjectByUser(Long userId);
}
