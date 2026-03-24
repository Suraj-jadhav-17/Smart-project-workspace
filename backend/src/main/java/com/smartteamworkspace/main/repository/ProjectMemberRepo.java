package com.smartteamworkspace.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartteamworkspace.main.entity.ProjectMember;

public interface ProjectMemberRepo extends JpaRepository<ProjectMember, Long> {
	
	Optional<ProjectMember> findByProject_IdAndUser_UserId(Long projectId, Long userId);
	boolean existsByProject_IdAndUser_UserId(Long projectId, Long userId);
	
	List<ProjectMember> findByProject_Id(Long projectId);
    
	List<ProjectMember> findByUser_UserId(Long userId);
	
}	
