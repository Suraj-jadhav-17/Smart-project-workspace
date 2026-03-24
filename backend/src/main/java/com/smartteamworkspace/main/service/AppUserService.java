package com.smartteamworkspace.main.service;

import java.util.List;

import com.smartteamworkspace.main.entity.AppUser;
import com.smartteamworkspace.main.enums.UserRole;
import com.smartteamworkspace.main.response.ResponseStructure;

public interface AppUserService {

	ResponseStructure<AppUser> registerUser(String username,String email,String password);
	
	ResponseStructure<AppUser> getUserByID(Long id);
	
	ResponseStructure<AppUser> getUserByEmail(String email);
	
	ResponseStructure<AppUser> getUserByUsername(String userName);
	
	ResponseStructure<List<AppUser>> getAllUsers(Long userID);
	
	ResponseStructure<AppUser> updateUser(Long userId,String newUserName,String newPassword,Long updatedById);
	
	ResponseStructure<AppUser> deActivateUser(Long id);
	
	ResponseStructure<AppUser> activateUser(Long id);
	
	ResponseStructure<AppUser> changeUserRole(Long userId,UserRole userRole,Long changedById);
	
	
	
	
}
