package com.smartteamworkspace.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartteamworkspace.main.entity.Task;

public interface TaskRepo extends JpaRepository<Task, Long>{
   List<Task> findByProject_Id(Long projectId);
   
   List<Task> findByAssignee_UserId(Long userId);
   List<Task> findByProject_Team_Id(Long teamId);
}
