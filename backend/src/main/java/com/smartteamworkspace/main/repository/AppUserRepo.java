package com.smartteamworkspace.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartteamworkspace.main.entity.AppUser;

public interface AppUserRepo extends JpaRepository<AppUser, Long>{
	boolean existsByEmail(String email);
	  boolean existsByUserName(String userName);
	    AppUser findByEmail(String email);
	    AppUser findByUserName(String userName);
}
