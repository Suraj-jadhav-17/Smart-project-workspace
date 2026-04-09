package com.smartteamworkspace.main.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartteamworkspace.main.entity.AppUser;
import com.smartteamworkspace.main.entity.Project;
import com.smartteamworkspace.main.entity.ProjectMember;
import com.smartteamworkspace.main.entity.Team;
import com.smartteamworkspace.main.enums.ProjectRole;
import com.smartteamworkspace.main.enums.ProjectStatus;
import com.smartteamworkspace.main.enums.ProjectVisibility;
import com.smartteamworkspace.main.enums.UserRole;
import com.smartteamworkspace.main.exception.AccessDeniedException;
import com.smartteamworkspace.main.exception.ConflictException;
import com.smartteamworkspace.main.exception.InvalidRequestException;
import com.smartteamworkspace.main.exception.ResourceNotFoundException;
import com.smartteamworkspace.main.repository.AppUserRepo;
import com.smartteamworkspace.main.repository.ProjectMemberRepo;
import com.smartteamworkspace.main.repository.ProjectRepo;
import com.smartteamworkspace.main.repository.TeamRepo;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.ProjectService;

@Service
public class ProjectServiceImp implements ProjectService{
	@Autowired
    private ProjectRepo projectRepo;
	
	@Autowired
	private AppUserRepo userRepo;
	
	@Autowired
	private TeamRepo teamRepo;
	
	@Autowired
	private ProjectMemberRepo projectMemRepo;
	
	private ResponseStructure<Project> createResponse(Project data,int statusCode,String massege){
		  ResponseStructure<Project> response = new ResponseStructure<>();
		  response.setData(data);
		  response.setMessage(massege);
		  response.setStatusCode(statusCode);
		return response;
	}
	@Override
	public ResponseStructure<Project> createProject(String name, ProjectVisibility visibility, LocalDate startDay,
			LocalDate deadline, Long createdById) {
		AppUser creater = userRepo.findById(createdById)
				.orElseThrow(()->new ResourceNotFoundException("User not found"));
				if(creater.getRole()!=UserRole.ADMIN) {
					throw new AccessDeniedException("Only admin can create projects");
				}
				if(name==null||name.isBlank()) {
					throw new InvalidRequestException("Project name is required");
				}
				if(deadline!=null&& startDay!=null&& deadline.isBefore(startDay)) {
					throw new InvalidRequestException("Deadline cannot be before start date");
				}
		 Project project = new Project();
		 project.setName(name);
		 project.setVisibility(visibility);
		 project.setStartDay(startDay);
		 project.setDeadline(deadline);
		 project.setStatus(ProjectStatus.ACTIVE);
		
		 Project savedProject=projectRepo.save(project);
		 
		 ProjectMember owner = new ProjectMember();
	       owner.setProject(savedProject);
	       owner.setRole(ProjectRole.PROJECT_OWNER);
	       owner.setUser(creater);
	       
	     projectMemRepo.save(owner);
	     
		return createResponse(savedProject, 201, "Project  created");
	}

	@Override
	public ResponseStructure<Project> updateProject(Long projectId, String name, ProjectVisibility visibility,
			LocalDate startDay, LocalDate deadLine, Long updatedByID) {
		Project project = projectRepo.findById(projectId)
						    .orElseThrow(()->new ResourceNotFoundException("Project not found"));
		ProjectMember actor = projectMemRepo.findByProject_IdAndUser_UserId(projectId, updatedByID)
				                .orElseThrow(()->new ResourceNotFoundException("User is not a member of this project"));
				if(actor.getRole()!= ProjectRole.PROJECT_OWNER && actor.getRole()!=ProjectRole.PROJECT_MANAGER) {
					throw new AccessDeniedException("Only project owner or manager can update project");
				}
				if(deadLine != null && startDay != null && deadLine.isBefore(startDay)){
				    throw new InvalidRequestException("Deadline cannot be before start date");
				}
				if(name != null && !name.isBlank()) {
					project.setName(name);
				}
				if(visibility!=null) {
					project.setVisibility(visibility);
				}
				if(startDay !=null) {
					project.setStartDay(startDay);
				}
				if(deadLine!=null) {
					project.setDeadline(deadLine);
				}
				
		Project updated = projectRepo.save(project);
		
		return createResponse(updated, 200, "Project updated");
	}

	@Override
	public ResponseStructure<List<Project>> getAllProject(Long teamId) {
		List<Project> projects =projectRepo.findByTeam_Id(teamId);
		ResponseStructure<List<Project>> response = new ResponseStructure<>();
		response.setData(projects);
		response.setStatusCode(200);
		response.setMessage(projects.isEmpty()? "No project found":"Projects fatched");
		return response ;
	}

	@Override
	public ResponseStructure<Project> findProjectById(Long id) {
		Project project= projectRepo.findById(id)
				           .orElseThrow(()-> new ResourceNotFoundException("Project not found"));
		return createResponse(project, 200, "Project found");
	}

	@Override
	public ResponseStructure<Project> deleteById(Long projectID, Long deletedById) {
		Project project = projectRepo.findById(projectID)
							.orElseThrow(()-> new ResourceNotFoundException("Project not found"));
		ProjectMember actor = projectMemRepo.findByProject_IdAndUser_UserId(projectID, deletedById)
				                 .orElseThrow(()->new ResourceNotFoundException("User is not a member of this project"));
				if(actor.getRole()!=ProjectRole.PROJECT_OWNER) {
					throw new AccessDeniedException("Only project owner can delete project");
					
				}
				projectRepo.delete(project);
				
		return createResponse(project, 200, "Project deleted");
		
	}

	@Override
	public ResponseStructure<Project> changeProjectStatus(Long projectId, ProjectStatus newStatus, Long updatedById) {
		Project project = projectRepo.findById(projectId)
							.orElseThrow(()-> new ResourceNotFoundException("Project not found"));
		ProjectMember actor = projectMemRepo.findByProject_IdAndUser_UserId(projectId, updatedById)
								.orElseThrow(()-> new ResourceNotFoundException("User is not a member of this project"));
				if(actor.getRole()!=ProjectRole.PROJECT_OWNER && actor.getRole()!=ProjectRole.PROJECT_MANAGER) {
					throw new AccessDeniedException("Only owner or manager can change project status");
				}
		project.setStatus(newStatus);
		
		Project updated = projectRepo.save(project);
		
		return createResponse(updated, 200, "Project status updated");
	}
	@Override
	public ResponseStructure<Project> addTeamToProject(Long projectId, Long TeamId, Long userId) {
		Project project = projectRepo.findById(projectId)
							.orElseThrow(()->new ResourceNotFoundException("Project not found"));
		Team team = teamRepo.findById(TeamId)
						.orElseThrow(()->new ResourceNotFoundException("Team not found"));
		ProjectMember member= projectMemRepo.findByProject_IdAndUser_UserId(projectId, userId)
				                   .orElseThrow(()->new ResourceNotFoundException("User is not a member of this project"));
		if(project.getTeam() != null){
		    throw new ConflictException("Project already belongs to a team");
		}
				if(member.getRole()!=ProjectRole.PROJECT_OWNER && member.getRole()!=ProjectRole.PROJECT_MANAGER) {
					throw new AccessDeniedException("Only owner or manager can add a team to project");
					
				}
		project.setTeam(team);
		Project updated = projectRepo.save(project);
		
		return createResponse(updated, 200, "Team added to project");
	}
	@Override
	public ResponseStructure<List<Project>> getAllProjectByUser(Long userId) {
		AppUser user = userRepo.findById(userId)
				         .orElseThrow(()-> new ResourceNotFoundException("User not found") );
		   ResponseStructure<List<Project>> response= new ResponseStructure<>();
		
		 List<ProjectMember> memberships = projectMemRepo.findByUser_UserId(userId);
	     List<Project> projects = memberships.stream()
	    		  .map(ProjectMember:: getProject)
	    		  .toList();
	    		  
		response.setData(projects);
		response.setStatusCode(200);
		response.setMessage("Projects fetched");
		
		return response;
	}
	@Override
	public ResponseStructure<List<Project>> getProjectsByUser(Long userId) {
		AppUser user = userRepo.findById(userId)
		         .orElseThrow(()-> new ResourceNotFoundException("User not found") );
		ResponseStructure<List<Project>> response= new ResponseStructure<>();
		
		if(user.getRole()==UserRole.ADMIN) {
			response.setData(projectRepo.findAll());
			
		}else {
			List<Project> projects= projectRepo.findAll().stream().filter(project->project.getVisibility()==ProjectVisibility.PUBLIC 
					|| project.getMembers().contains(user)).collect(Collectors.toList());
			
			response.setData(projects);
			
		}
		response.setStatusCode(200);
		response.setMessage("Project fetched");
		return response;
	}

   
	
}
