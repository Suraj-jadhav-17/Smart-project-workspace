package com.smartteamworkspace.main.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartteamworkspace.main.entity.AppUser;
import com.smartteamworkspace.main.entity.Project;
import com.smartteamworkspace.main.entity.ProjectMember;
import com.smartteamworkspace.main.entity.Task;
import com.smartteamworkspace.main.enums.ProjectRole;
import com.smartteamworkspace.main.enums.TaskPriority;
import com.smartteamworkspace.main.enums.TaskStatus;
import com.smartteamworkspace.main.exception.AccessDeniedException;
import com.smartteamworkspace.main.exception.InvalidRequestException;
import com.smartteamworkspace.main.exception.ResourceNotFoundException;
import com.smartteamworkspace.main.repository.AppUserRepo;
import com.smartteamworkspace.main.repository.ProjectMemberRepo;
import com.smartteamworkspace.main.repository.ProjectRepo;
import com.smartteamworkspace.main.repository.TaskRepo;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.TaskService;

import jakarta.transaction.Transactional;

@Service
public class TaskServiceImp implements TaskService{
 @Autowired
 private TaskRepo taskRepo;
 
 @Autowired
 private ProjectRepo projectRepo;
 
 @Autowired
 private AppUserRepo userRepo;
 
 @Autowired
 private ProjectMemberRepo projectMemberRepo;
 
 private ResponseStructure<Task> createResponse(Task data, int StatusCode,String massege){
	 ResponseStructure<Task> response= new ResponseStructure<>();
	 response.setData(data);
	 response.setMessage(massege);
	 response.setStatusCode(StatusCode);
	 return response;
 }
 @Transactional
 @Override
 public ResponseStructure<Task> createTask(Long projectId, String title, String description, TaskPriority priority,
		LocalDate dueDate, Long createdById) {
	   Project project = projectRepo.findById(projectId)
			               .orElseThrow(()->new ResourceNotFoundException("Project not found"));
	   AppUser user = userRepo.findById(createdById)
			   			.orElseThrow(()->new ResourceNotFoundException("User not found"));
	  
		   ProjectMember member =projectMemberRepo.findByProject_IdAndUser_UserId(projectId, createdById)
				                   .orElseThrow(()->new AccessDeniedException("User is not part of this project"));
		   if(member.getRole()!=ProjectRole.PROJECT_OWNER && member.getRole()!=ProjectRole.PROJECT_MANAGER) {
			      throw new AccessDeniedException("Only project owner or manager can create tasks");
		   }
		   if(dueDate!=null && dueDate.isBefore(LocalDate.now())) {
			   throw new InvalidRequestException("Due date cannot be in the past");
			   
		   }
		   if(title == null || title.isBlank()) {
			    throw new InvalidRequestException("Task title is required");
		   }
	   Task task = new Task();
	   task.setTitle(title);
	   task.setDescription(description);
	   task.setPriority(priority);
	   task.setDueDate(dueDate);
	   task.setProject(project);
	   task.setCreatedBy(user);
	   task.setStatus(TaskStatus.TODO);
	   
	   Task added = taskRepo.save(task);
	return createResponse(added, 201, "Task created");
 }
 @Transactional
 @Override
 public ResponseStructure<Task> updateTaskDetailes(Long taskId, String title, String description, LocalDate dueDate,
		Long updatedById) {
      Task task= taskRepo.findById(taskId)
    		     .orElseThrow(()->new ResourceNotFoundException("Task not found"));
      ProjectMember actor = projectMemberRepo.findByProject_IdAndUser_UserId(task.getProject().getId(), updatedById)
    		                   .orElseThrow(()->new AccessDeniedException("User is not part of this project"));
      			if(actor.getRole()!=ProjectRole.PROJECT_OWNER&& actor.getRole()!=ProjectRole.PROJECT_MANAGER) {
      				throw new AccessDeniedException("Only project owner or manager can update task");
      			}
      			if(dueDate != null && dueDate.isBefore(LocalDate.now())) {
     			   throw new InvalidRequestException("Due date cannot be in the past");   
     		   }
      			if(title != null && !title.isBlank()) {
      			    task.setTitle(title);
      			}		
                
      			if(description != null){
      			    task.setDescription(description);
      			}
              if(dueDate!=null) {
            	  task.setDueDate(dueDate);
              }
     Task updated = taskRepo.save(task);
    	
	return createResponse(updated, 200, "Task updated");
 }
 
 @Transactional
 @Override
 public ResponseStructure<Task> changeTaskStatus(Long taskId, TaskStatus newStatus, Long updatedById) {
	  Task task = taskRepo.findById(taskId)
			  		.orElseThrow(()->new ResourceNotFoundException("Task not found"));
	  
	  if(task.getAssignee()==null||!task.getAssignee().getUserId().equals(updatedById)) {
		  throw new AccessDeniedException("Only task assignee can change status");
	  }
	  task.setStatus(newStatus);
	  
	  Task update = taskRepo.save(task);
	return createResponse(update, 200, "Task status updated");
 }

 @Transactional
 @Override
 public ResponseStructure<Task> changeTaskPriority(Long taskId, TaskPriority newPriority, Long updatedById) {
	Task task=taskRepo.findById(taskId)
			    .orElseThrow(()->new ResourceNotFoundException("Task not found"));
	ProjectMember member= projectMemberRepo.findByProject_IdAndUser_UserId(task.getProject().getId(), updatedById)
			                      .orElseThrow(()->new AccessDeniedException("User is not part of this project"));
						if(member.getRole()!=ProjectRole.PROJECT_OWNER&& member.getRole()!=ProjectRole.PROJECT_MANAGER) {
							throw new AccessDeniedException("Only project owner or manager can change task priority");
						}
		task.setPriority(newPriority);
		Task updated = taskRepo.save(task);
		
	return createResponse(updated, 200, "Task priority updated");
 }
 @Transactional
 @Override
 public ResponseStructure<Task> assignTask(Long taskId, Long assigneeId, Long assignById) {
	Task task = taskRepo.findById(taskId)
					.orElseThrow(()->new ResourceNotFoundException("Task not found"));
	AppUser user= userRepo.findById(assigneeId)
					.orElseThrow(()->new ResourceNotFoundException("User not found"));
	ProjectMember actor =projectMemberRepo.findByProject_IdAndUser_UserId(task.getProject().getId(), assignById)
							.orElseThrow(()->new AccessDeniedException("User is not part of this project"));
	projectMemberRepo.findByProject_IdAndUser_UserId(task.getProject().getId(), assigneeId)
							.orElseThrow(()->new InvalidRequestException("Assignee must be part of the project"));
			if(actor.getRole()!=ProjectRole.PROJECT_OWNER && actor.getRole()!=ProjectRole.PROJECT_MANAGER) {
				throw new AccessDeniedException("Only project owner or manager can assign tasks");
			}
	task.setAssignee(user);
	Task updated =taskRepo.save(task);
	
	return createResponse(updated, 200, "Task assigned");
 }
 @Transactional
 @Override
 public ResponseStructure<Task> deleteTask(Long taskId, Long deletedById) {
    Task task = taskRepo.findById(taskId)
	            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    ProjectMember actor= projectMemberRepo.findByProject_IdAndUser_UserId(task.getProject().getId(), deletedById)
            .orElseThrow(()->new AccessDeniedException("User is not part of this project"));
			if(actor.getRole()!=ProjectRole.PROJECT_OWNER&& actor.getRole()!=ProjectRole.PROJECT_MANAGER) {
				throw new AccessDeniedException("Only project owner or manager can delete tasks");
			}
	taskRepo.delete(task);
	
	return createResponse(task, 200, "Task deleted");
 }

 @Override
 public ResponseStructure<List<Task>> getTasksByProject(Long projectId) {
	 projectRepo.findById(projectId)
     .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
	List<Task> tasks = taskRepo.findByProject_Id(projectId);
	ResponseStructure<List<Task>> response= new ResponseStructure<>();
	response.setData(tasks);
	response.setStatusCode(200);
	response.setMessage(tasks.isEmpty()? "No task found":"Tasks fetched");
	return response;
 }

 @Override
 public ResponseStructure<Task> getTaskById(Long taskId) {
	 Task task = taskRepo.findById(taskId)
	            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
	return createResponse(task, 200, "Task found");
 }

 @Override
 public ResponseStructure<List<Task>> getTasksAssignedToUser(Long userId) {
	 userRepo.findById(userId)
	     .orElseThrow(()->new ResourceNotFoundException("User not found"));
	List<Task> tasks =taskRepo.findByAssignee_UserId(userId);
	ResponseStructure<List<Task>> response= new ResponseStructure<>();
	response.setData(tasks);
	response.setStatusCode(200);
	response.setMessage(tasks.isEmpty()? "No task found":"Tasks fetched");
	return response ;
 }
 
 
	
}
