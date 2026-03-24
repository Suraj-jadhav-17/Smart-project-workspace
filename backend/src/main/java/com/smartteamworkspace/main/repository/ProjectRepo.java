package com.smartteamworkspace.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartteamworkspace.main.entity.Project;

public interface ProjectRepo  extends JpaRepository<Project, Long>{
  
	List<Project> findByTeam_Id(Long teamId);
	
}
