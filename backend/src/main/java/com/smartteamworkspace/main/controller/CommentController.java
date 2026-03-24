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

import com.smartteamworkspace.main.entity.Comment;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	@Autowired
	private CommentService comment;
	
	@PostMapping("/add")
	public ResponseStructure<Comment> addComment(
            @RequestParam Long taskId,
            @RequestParam Long userId,
            @RequestParam String content) {
		return comment.addComment(taskId, userId, content);
	}
	
	@PutMapping("/update")
	public ResponseStructure<Comment> updateComment(
            @RequestParam Long commentId,
            @RequestParam String content,
            @RequestParam Long updatedById) {
		return comment.modifyComment(commentId, updatedById, content);
	}
	
	 @DeleteMapping("/delete")
	 public ResponseStructure<Comment>  deleteComment(
	            @RequestParam Long commentId,
	            @RequestParam Long deletedById) {
		 return comment.deleteById(commentId, deletedById);
	 }
	 @GetMapping("/task/{taskId}")
	 public ResponseStructure<List<Comment>>  getCommentsByTask(
	            @PathVariable Long taskId) {
		 return comment.getAllComment(taskId);
	 }
	 @GetMapping("/get/{commentId}")
	 public ResponseStructure<Comment> getCommentById(
	            @PathVariable Long commentId) {
		 return comment.getCommentById(commentId);
	 }


}
