package com.smartteamworkspace.main.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartteamworkspace.main.entity.AppUser;
import com.smartteamworkspace.main.enums.UserRole;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.AppUserService;

@RestController
@RequestMapping("/api/users")
public class AppUserController {
     
	@Autowired
	private AppUserService appUserService;
	
	@PostMapping("/register")
	public ResponseStructure<AppUser> registerUser(@RequestParam String username, @RequestParam String email,
            @RequestParam String password){
		return appUserService.registerUser(username, email, password);
	}
	
	@GetMapping("/{id}")
	public ResponseStructure<AppUser> getUserById(@PathVariable Long id){
		return appUserService.getUserByID(id);
	}
	
	
	@GetMapping("/email/{email}")
	public ResponseStructure<AppUser> getUserByEmail(@PathVariable String email){
		return  appUserService.getUserByEmail(email);
	}
	
	@GetMapping("/username/{username}")
	public ResponseStructure<AppUser> getUserByUsername(@PathVariable String username){
		return appUserService.getUserByUsername(username);
	}
	@PutMapping("update/{userId}")
	public ResponseStructure<AppUser> upadteUser(@PathVariable Long userId, @RequestParam String newUserName,
            @RequestParam String newPassword,
            @RequestParam Long updatedById){
            	return appUserService.updateUser(userId, newUserName, newPassword, updatedById);
            }
	
	@PutMapping("/{userId}/activate")
	public ResponseStructure<AppUser> activateUser(@PathVariable Long userId){
		   return appUserService.activateUser(userId);
	}
	@PutMapping("/{userId}/deactivate")
	public ResponseStructure<AppUser> deactivateUser(@PathVariable Long userId){
		return appUserService.deActivateUser(userId);
	}
	
	 @PutMapping("/{userId}/role")
	    public ResponseStructure<AppUser> changeUserRole(@PathVariable Long userId,
	                                                                     @RequestParam UserRole role,
	                                                                     @RequestParam Long changedById){
		 return appUserService.changeUserRole(userId, role, changedById);
	 }
 }
