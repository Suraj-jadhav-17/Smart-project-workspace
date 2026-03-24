package com.smartteamworkspace.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartteamworkspace.main.entity.Team;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.TeamService;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    @Autowired
	private TeamService teamService;
    
    @PostMapping("/create")
    public ResponseStructure<Team> createTeam(@RequestParam Long createdById,@RequestParam String name){
    	return teamService.createTeam(createdById, name);
 
    }
    
    @PutMapping("/update")
    public ResponseStructure<Team> updateTeam(@RequestParam Long teamId,@RequestParam String newName,@RequestParam Long updatedById){
    	return teamService.updateTeam(teamId, newName, updatedById);
    }
    @GetMapping("/get/{teamId}")
    public ResponseStructure<Team> getTeamById(@PathVariable Long teamId){
    	return teamService.getTeamById(teamId);
    }
    @GetMapping("/user/{userId}")
    public ResponseStructure<List<Team>> getTeamByUser(@PathVariable Long userId){
    	return teamService.getTeamsByUser(userId);
    }
    @DeleteMapping("/delete")
    public ResponseStructure<Team> deleteTeam(@RequestParam Long teamId,@RequestParam Long deletedById){
    	return teamService.deleteTeam(teamId, deletedById);

    }
    @DeleteMapping("/leave")
    public ResponseStructure<String> leaveTeam(@RequestParam Long teamId,@RequestParam Long userId){
    	return teamService.leaveTeam(teamId, userId);
    }
    
    @PutMapping("/transfer-ownership")
    public ResponseStructure<String> transferOwnership(@RequestParam Long teamId,@RequestParam Long currentOwnerId,@RequestParam Long newOwnerId){
    	return teamService.transferOwnership(teamId, currentOwnerId, newOwnerId);
    }
}
