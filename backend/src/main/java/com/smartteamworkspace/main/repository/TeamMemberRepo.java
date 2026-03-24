package com.smartteamworkspace.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartteamworkspace.main.entity.TeamMember;
import com.smartteamworkspace.main.enums.TeamRole;

public interface TeamMemberRepo extends JpaRepository<TeamMember, Long> {
   Optional<TeamMember> findByTeam_IdAndUser_UserId(Long teamId, Long userId);
   
   boolean existsByTeam_IdAndUser_UserId(Long teamId,Long userId);
   List<TeamMember> findByUser_UserId(Long userId);
   long countByTeam_IdAndRole(Long teamId, TeamRole role);
}
