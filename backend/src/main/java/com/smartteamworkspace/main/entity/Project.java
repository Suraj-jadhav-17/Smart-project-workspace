package com.smartteamworkspace.main.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartteamworkspace.main.enums.ProjectStatus;
import com.smartteamworkspace.main.enums.ProjectVisibility;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity

public class Project {
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public ProjectVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(ProjectVisibility visibility) {
		this.visibility = visibility;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getStartDay() {
		return startDay;
	}

	public void setStartDay(LocalDate startDay) {
		this.startDay = startDay;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public List<ProjectMember> getMembers() {
		return members;
	}

	public void setMembers(List<ProjectMember> members) {
		this.members = members;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id ;
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    
    @Enumerated(EnumType.STRING)
    private ProjectVisibility visibility;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Team team;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    private LocalDate startDay;
    
    private LocalDate deadline;
    @JsonIgnore
    @OneToMany(mappedBy = "project" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProjectMember> members = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();
    
    
}
