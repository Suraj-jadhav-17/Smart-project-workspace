package com.smartteamworkspace.main.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartteamworkspace.main.enums.AccountStatus;
import com.smartteamworkspace.main.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class AppUser {

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<TeamMember> getTeamMemberships() {
		return teamMemberships;
	}

	public void setTeamMemberships(List<TeamMember> teamMemberships) {
		this.teamMemberships = teamMemberships;
	}

	public List<ProjectMember> getProjectMemberships() {
		return projectMemberships;
	}

	public void setProjectMemberships(List<ProjectMember> projectMemberships) {
		this.projectMemberships = projectMemberships;
	}

	public List<Task> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(List<Task> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long userId;
	
	@Column(unique = true,nullable = false)
	private String userName;
	
	@Column(unique = true,nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus accountStatus;
	
	@CreationTimestamp
	@Column(nullable = false,updatable = false)
	private LocalDateTime createdAt;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user" , cascade = CascadeType.ALL,orphanRemoval = true)
	private List<TeamMember> teamMemberships= new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
	private List<ProjectMember> projectMemberships = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "assignee")
	private List<Task> assignedTasks = new ArrayList<>();
	
	

}
