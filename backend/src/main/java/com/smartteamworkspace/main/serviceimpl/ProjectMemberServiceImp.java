package com.smartteamworkspace.main.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartteamworkspace.main.entity.AppUser;
import com.smartteamworkspace.main.entity.Project;
import com.smartteamworkspace.main.entity.ProjectMember;

import com.smartteamworkspace.main.enums.ProjectRole;
import com.smartteamworkspace.main.exception.AccessDeniedException;
import com.smartteamworkspace.main.exception.ConflictException;
import com.smartteamworkspace.main.exception.InvalidRequestException;
import com.smartteamworkspace.main.exception.ResourceNotFoundException;
import com.smartteamworkspace.main.repository.AppUserRepo;
import com.smartteamworkspace.main.repository.ProjectMemberRepo;
import com.smartteamworkspace.main.repository.ProjectRepo;
import com.smartteamworkspace.main.repository.TeamMemberRepo;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.ProjectMemberService;

@Service
public class ProjectMemberServiceImp implements ProjectMemberService{

    
    @Autowired
	private ProjectMemberRepo projectMemRepo;
	@Autowired
	private ProjectRepo projectRepo;
	@Autowired
	private AppUserRepo userRepo;
	
	@Autowired
	private TeamMemberRepo teamMemberRepo;
	
    private ResponseStructure<ProjectMember> createResponse(ProjectMember data, int statusCode,String massege){
		 ResponseStructure<ProjectMember> response = new ResponseStructure<>();
		 response.setData(data);
		 response.setStatusCode(statusCode);
		 response.setMessage(massege);
		 return response;	 
	 }
	 
	@Override
	public ResponseStructure<ProjectMember> addMember(Long projectId, Long userId, ProjectRole role, Long addedbyid) {
		Optional<Project> projectOptional= projectRepo.findById(projectId);
				if(!projectOptional.isPresent()) {
					throw new ResourceNotFoundException("Project not found");
				}
		Optional<AppUser> userOptional=userRepo.findById(userId);
				if(!userOptional.isPresent()) {
					throw new ResourceNotFoundException("User not found");
				}
		Optional<ProjectMember> managerOptinal=projectMemRepo.findByProject_IdAndUser_UserId(projectId, addedbyid);
				if(!managerOptinal.isPresent()) {
					throw new ResourceNotFoundException("Manager not found");
				}
				if(projectOptional.get().getTeam() != null) {

				    boolean isTeamMember =
				            teamMemberRepo.existsByTeam_IdAndUser_UserId(
				            		projectOptional.get().getTeam().getId(),
				                    userId
				            );

				    if(!isTeamMember){
				        throw new InvalidRequestException(
				            "User must be a member of the project team to join the project"
				        );
				    }
				}
		Optional<ProjectMember> exsisting = projectMemRepo.findByProject_IdAndUser_UserId(projectId, userId);
				if(exsisting.isPresent()) {
					throw new ConflictException("User is already in project");
				}
		AppUser user=userOptional.get();
		Project project=projectOptional.get();//project
		ProjectMember manager = managerOptinal.get();//member
				if(manager.getRole()!=ProjectRole.PROJECT_MANAGER && manager.getRole()!=ProjectRole.PROJECT_OWNER ) {
					throw new AccessDeniedException("You do not have permission to add members");
				}
				if(manager.getRole()==ProjectRole.PROJECT_MANAGER && (role==ProjectRole.PROJECT_OWNER ||role==ProjectRole.PROJECT_MANAGER)) {
					throw new AccessDeniedException("Manager cannot assign this role");
				}
				if(role == ProjectRole.PROJECT_OWNER){
				    throw new ConflictException("Project owner already exists");
				}
		ProjectMember member= new ProjectMember();
		member.setProject(project);
		member.setRole(role);
		member.setUser(user);
		ProjectMember addedMem=projectMemRepo.save(member);
		
		return createResponse(addedMem, 200, "Project member added");
	}

	@Override
	public ResponseStructure<ProjectMember> updateMember(Long projectId, Long memberId, ProjectRole newRole,
			Long updatedById) {
		ProjectMember actor = projectMemRepo
		        .findByProject_IdAndUser_UserId(projectId, updatedById)
		        .orElseThrow(() -> new ResourceNotFoundException("Actor is not a member of the project"));
				if(actor.getRole()!=ProjectRole.PROJECT_OWNER && actor.getRole()!=ProjectRole.PROJECT_MANAGER) {
					throw new AccessDeniedException("You do not have permission to update member roles");
				}
	    projectRepo.findById(projectId)
	    		 .orElseThrow(()-> new ResourceNotFoundException("Project not found"));
	   
	   ProjectMember member= projectMemRepo.findById(memberId)
			   .orElseThrow(()->new ResourceNotFoundException("Member not found"));
				if(!member.getProject().getId().equals(projectId)) {
					throw new InvalidRequestException("Member does not belong to this project");
				}     
				if(actor.getRole()==ProjectRole.PROJECT_MANAGER && (newRole==ProjectRole.PROJECT_OWNER ||newRole==ProjectRole.PROJECT_MANAGER)) {
					throw new AccessDeniedException("Manager cannot assign this role");
				}
		member.setRole(newRole);
		ProjectMember updatedMember=projectMemRepo.save(member);
				
		return createResponse(updatedMember, 200, "Project member updated");
	}

	@Override
	public ResponseStructure<List<ProjectMember>> getAllProjectMember(Long projectId) {
		projectRepo.findById(projectId)
				         .orElseThrow(()->new ResourceNotFoundException("Project not found"));
		List<ProjectMember> members=projectMemRepo.findByProject_Id(projectId);
				
		ResponseStructure<List<ProjectMember>> response =new ResponseStructure<>();
		response.setData(members);
		response.setStatusCode(200);
		response.setMessage(members.isEmpty()?"No member found":"Members fetched"); 
		return response;
	}

	@Override
	public ResponseStructure<ProjectMember> removeMemberById(Long memberId, Long removedById) {
		ProjectMember member=projectMemRepo.findById(memberId)
				             .orElseThrow(()->new ResourceNotFoundException("Member not found"));
				if(member.getRole()==ProjectRole.PROJECT_OWNER) {
					throw new AccessDeniedException("Project owner cannot be removed");
				}
				ProjectMember actor = projectMemRepo
						.findByProject_IdAndUser_UserId(member.getProject().getId(), removedById)
						.orElseThrow(() -> new ResourceNotFoundException("Actor is not a member of the project"));
				if(actor.getRole()!=ProjectRole.PROJECT_OWNER && actor.getRole()!=ProjectRole.PROJECT_MANAGER) {
					throw new AccessDeniedException("You do not have permission to remove this member");
				}
				if(actor.getRole()==ProjectRole.PROJECT_MANAGER && 
						   (member.getRole()==ProjectRole.PROJECT_MANAGER || member.getRole()==ProjectRole.PROJECT_OWNER)) {
						    throw new AccessDeniedException("Manager cannot remove this role");
						}
				projectMemRepo.delete(member);
		return createResponse(member, 200, "Project member removed");
	}

	@Override
	public boolean isUserProjectMember(Long projectId, Long userId) {
		
		return projectMemRepo.findByProject_IdAndUser_UserId(projectId, userId).isPresent();
	}

}
