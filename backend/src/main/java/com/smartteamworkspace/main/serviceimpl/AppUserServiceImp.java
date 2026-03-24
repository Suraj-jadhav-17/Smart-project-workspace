package com.smartteamworkspace.main.serviceimpl;

import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartteamworkspace.main.entity.AppUser;
import com.smartteamworkspace.main.enums.AccountStatus;
import com.smartteamworkspace.main.enums.UserRole;
import com.smartteamworkspace.main.exception.AccessDeniedException;
import com.smartteamworkspace.main.exception.ConflictException;
import com.smartteamworkspace.main.exception.InvalidRequestException;
import com.smartteamworkspace.main.exception.ResourceNotFoundException;
import com.smartteamworkspace.main.repository.AppUserRepo;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.AppUserService;
@Service
public class AppUserServiceImp implements AppUserService{
	@Autowired
   private AppUserRepo userRepo;
	
	@Autowired
   private PasswordEncoder passwordEncoder;
	
	public ResponseStructure<AppUser> registerUser(String username,String email,String password) {
				if(userRepo.existsByUserName(username)) {
					throw new ConflictException("UserName already taken");
				}
				if(userRepo.existsByEmail(email)) {
					throw new ConflictException("Email already exist!");
				}
				if(password==null|| password.isBlank()) {
					throw new InvalidRequestException("Password can not be empty");
				}
				if(password.length()<8) {
					throw new InvalidRequestException("Password must contain at least 8 characters");
				}
		AppUser user= new AppUser();
		user.setUserName(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password)) ;// to encrypt the password
		
			    if(userRepo.count()==0) {
			    	user.setRole(UserRole.ADMIN);
			    }else {
			    	user.setRole(UserRole.MEMBER);
			    }
	    user.setAccountStatus(AccountStatus.ACTIVE);
	    AppUser addedUser=userRepo.save(user);
	    ResponseStructure<AppUser> userResp= new ResponseStructure<>();
	    userResp.setData(addedUser);
	    userResp.setMessage("User registered");
	    userResp.setStatusCode(201);
		return userResp;
	}

	@Override
	public ResponseStructure<AppUser> getUserByID(Long id) {
		Optional<AppUser> optional=userRepo.findById(id);
				if(!optional.isPresent()) {
					throw new ResourceNotFoundException("User not found");
				}
		AppUser user= optional.get();
		ResponseStructure<AppUser> response= new ResponseStructure<>();
		
			response.setStatusCode(200);
			response.setMessage("User found");
			response.setData(user);
			return response;
		
	}

	@Override
	public ResponseStructure<AppUser> getUserByEmail(String email) {
		AppUser user= userRepo.findByEmail(email);
		ResponseStructure<AppUser> response = new ResponseStructure<>();
		
		if(user!=null) {
			response.setData(user);
			response.setMessage("User found");
			response.setStatusCode(200);
			return response;
		}else {
			throw new ResourceNotFoundException("User not found");
		}
		
	}

	@Override
	public ResponseStructure<AppUser> getUserByUsername(String userName) {
		AppUser user= userRepo.findByUserName(userName);
		ResponseStructure<AppUser> response = new ResponseStructure<>();
		
		if(user!=null) {
			response.setData(user);
			response.setMessage("User found");
			response.setStatusCode(200);
			return response;
		}else {
			throw new ResourceNotFoundException("User not found");
		}
		
	}

	@Override
	public ResponseStructure<List<AppUser>> getAllUsers(Long userId) {
		Optional<AppUser> optional= userRepo.findById(userId);
				if(!optional.isPresent()) {
					throw new ResourceNotFoundException("User not found");
				}
		AppUser user= optional.get();
				if(user.getRole()!= UserRole.ADMIN) {
				   throw new AccessDeniedException("Only admin can access all users");
				}
		List<AppUser> users= userRepo.findAll();
		ResponseStructure<List<AppUser>> response = new ResponseStructure<>();
		response.setData(users);
		response.setStatusCode(200);
	    response.setMessage(users.isEmpty()?"No user present" : " Users fetched");
			
		return response;
	}

	@Override
	public ResponseStructure<AppUser> updateUser(Long userId, String newUserName, String newPassword,Long updatedById) {
		Optional<AppUser> optional = userRepo.findById(userId);
		
				if(!optional.isPresent()) {
					throw new ResourceNotFoundException("User not found");
				}
				
		Optional<AppUser> optionalBy = userRepo.findById(updatedById);
		
				if(!optionalBy.isPresent()) {
					throw new ResourceNotFoundException("Updating user not found");
				}
				
		AppUser updatedBy = optionalBy.get();//updating one
		AppUser newUser= optional.get();//user
		
		
				if(updatedBy.getRole() != UserRole.ADMIN &&!updatedBy.getUserId().equals(newUser.getUserId())){
		
					throw new AccessDeniedException("Not authorized to update this user");
						}
				if(newUserName==null|| newUserName.isBlank()) {
					throw new InvalidRequestException("Username cannot be empty");
				}
				if(!newUser.getUserName().equals(newUserName) 
				        && userRepo.existsByUserName(newUserName)) {
				    throw new ConflictException("Username already taken");
				}
				
		newUser.setUserName(newUserName);
		
				if(newPassword==null|| newPassword.isBlank()) {
					throw new InvalidRequestException("Password cannot be empty");
				}
				if(newPassword.length()<8) {
					throw new InvalidRequestException("Password must contain at least 8 characters");
				}
		newUser.setPassword(passwordEncoder.encode(newPassword));
		AppUser updatedUser= userRepo.save(newUser);
		ResponseStructure<AppUser> response = new ResponseStructure<>();
		response.setData(updatedUser);
		response.setStatusCode(200);
		response.setMessage("User updated");
		return response;
	}

	@Override
	public ResponseStructure<AppUser> deActivateUser(Long userId) {
		Optional<AppUser> optional = userRepo.findById(userId);
				if(!optional.isPresent()) {
					throw new ResourceNotFoundException("User not found");
				}
		AppUser user= optional.get();
				if(user.getAccountStatus()==AccountStatus.DISABLED) {
					throw new ConflictException("User already deactivated");
				}
		user.setAccountStatus(AccountStatus.DISABLED);
		AppUser updatedUser= userRepo.save(user);
		ResponseStructure<AppUser> response= new ResponseStructure<>();
		response.setData(updatedUser);
		response.setStatusCode(200);
		response.setMessage("User account deactivated");
		
		return response;
	}

	@Override
	public ResponseStructure<AppUser> activateUser(Long userId) {
		Optional<AppUser> optional = userRepo.findById(userId);
				if(!optional.isPresent()) {
					throw new ResourceNotFoundException("User not found");
				}
		AppUser user= optional.get();
				if(user.getAccountStatus()==AccountStatus.ACTIVE) {
					throw new ConflictException("User already activated");
				}
		user.setAccountStatus(AccountStatus.ACTIVE);
		AppUser updatedUser= userRepo.save(user);
		ResponseStructure<AppUser> response= new ResponseStructure<>();
		response.setData(updatedUser);
		response.setStatusCode(200);
		response.setMessage("User account activated");
		
		return response;
	}

	@Override
	public ResponseStructure<AppUser> changeUserRole(Long userId, UserRole userRole, Long changedById) {
		Optional<AppUser> optional = userRepo.findById(changedById);
				if(!optional.isPresent()) {
					throw new ResourceNotFoundException("Admin user not found");
				}
		AppUser admin= optional.get();//admin
				if(admin.getRole()!=UserRole.ADMIN) {
					throw new AccessDeniedException("Only admin can change roles");
				}
		Optional<AppUser> optionalUser= userRepo.findById(userId);//user
		
				if(!optionalUser.isPresent()) {
					throw new ResourceNotFoundException("User not found");
				}
		AppUser user= optionalUser.get();
		user.setRole(userRole);
		AppUser updatedUser=userRepo.save(user);
		ResponseStructure<AppUser> response=new ResponseStructure<>();
		response.setData(updatedUser);
		response.setStatusCode(200);
		response.setMessage("User role updated");
		return response;
	}
	
	

}
