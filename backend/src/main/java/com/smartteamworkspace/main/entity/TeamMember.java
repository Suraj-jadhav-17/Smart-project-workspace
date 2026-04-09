package com.smartteamworkspace.main.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartteamworkspace.main.enums.TeamRole;

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


@Entity
public class TeamMember {
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public TeamRole getRole() {
		return role;
	}

	public void setRole(TeamRole role) {
		this.role = role;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public LocalDateTime getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(LocalDateTime joinedAt) {
		this.joinedAt = joinedAt;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id ;
    
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@JsonIgnore
    private Team team;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
   
    private AppUser user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamRole role;
    
    @Column(nullable = false)
    private String designation;
    
    @CreationTimestamp
    private LocalDateTime joinedAt;
}
