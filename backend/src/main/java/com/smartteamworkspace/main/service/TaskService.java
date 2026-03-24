package com.smartteamworkspace.main.service;

import java.time.LocalDate;
import java.util.List;

import com.smartteamworkspace.main.entity.Task;
import com.smartteamworkspace.main.enums.TaskPriority;
import com.smartteamworkspace.main.enums.TaskStatus;
import com.smartteamworkspace.main.response.ResponseStructure;

public interface TaskService {

	ResponseStructure<Task> createTask(Long projectId,String title,String description,TaskPriority priority,LocalDate dueDate,Long createdById);
	
	ResponseStructure<Task> updateTaskDetailes(Long taskId, String title,String description,LocalDate dueDate,Long updatedById);
	ResponseStructure<Task> changeTaskStatus(Long taskId,TaskStatus newStatus,Long upadtedById);
	ResponseStructure<Task> changeTaskPriority(Long taskId, TaskPriority newPriority,Long updatedById);
	ResponseStructure<Task> assignTask(Long taskId,Long assigneeId,Long assignById);
	ResponseStructure<Task> deleteTask(Long taskId, Long deletedById);
	ResponseStructure<List<Task>> getTasksByProject(Long projectId);
	ResponseStructure<Task> getTaskById(Long id);
	ResponseStructure<List<Task>> getTasksAssignedToUser(Long userId);
	
	
}
