package com.smartteamworkspace.main.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartteamworkspace.main.entity.Task;
import com.smartteamworkspace.main.enums.TaskPriority;
import com.smartteamworkspace.main.enums.TaskStatus;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	@Autowired
    private TaskService taskService;

	@PostMapping("/create")
	public ResponseStructure<Task> createTask(
            @RequestParam Long projectId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam TaskPriority priority,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam Long createdById) {
		return taskService.createTask(projectId, title, description, priority, dueDate, createdById);
	}
	
	 @PutMapping("/update")
	public ResponseStructure<Task> updateTaskDetails(
            @RequestParam Long taskId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam Long updatedById) {
		return taskService.updateTaskDetailes(taskId, title, description, dueDate, updatedById);
	}
	 
	 @PutMapping("/status")
	 public ResponseStructure<Task> changeTaskStatus(
	            @RequestParam Long taskId,
	            @RequestParam TaskStatus status,
	            @RequestParam Long updatedById) {
		 return taskService.changeTaskStatus(taskId, status, updatedById);
	 }
	 
	 @PutMapping("/priority")
	 public ResponseStructure<Task> changeTaskPriority(
	            @RequestParam Long taskId,
	            @RequestParam TaskPriority priority,
	            @RequestParam Long updatedById) {
		 return taskService.changeTaskPriority(taskId, priority, updatedById);
	 }
	 @PutMapping("/assign")
	 public ResponseStructure<Task> assignTask(
	            @RequestParam Long taskId,
	            @RequestParam Long assigneeId,
	            @RequestParam Long assignById) {
		 return taskService.assignTask(taskId, assigneeId, assignById);
	 }
	 @DeleteMapping("/delete")
	 public ResponseStructure<Task> deleteTask(
	            @RequestParam Long taskId,
	            @RequestParam Long deletedById) {
		 return taskService.deleteTask(taskId, deletedById);
	 }
	 @GetMapping("/project/{projectId}")
	 public ResponseStructure<List<Task>>  getTasksByProject(
	            @PathVariable Long projectId) {
		 return taskService.getTasksByProject(projectId);
	 }
	 @GetMapping("/get/{taskId}")
	 public ResponseStructure<Task> getTaskById(
			 @PathVariable Long taskId) {
		 return taskService.getTaskById(taskId);
	 }
	    @GetMapping("/user/{userId}")
	 public ResponseStructure<List<Task>> getTaskAssignedToUser(@PathVariable Long userId){
		 return taskService.getTasksAssignedToUser(userId);
	 }
}
