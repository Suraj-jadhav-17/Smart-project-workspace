package com.smartteamworkspace.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartteamworkspace.main.entity.Team;
import com.smartteamworkspace.main.entity.TeamMember;
import com.smartteamworkspace.main.enums.TeamRole;

public interface TeamRepo extends JpaRepository<Team, Long> {
	
	
}
