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
import org.springframework.web.multipart.MultipartFile;

import com.smartteamworkspace.main.entity.TaskAttachment;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.TaskAttachmentService;

@RestController
@RequestMapping("/api/attachment")
public class TaskAttachmentController {
	@Autowired
	private TaskAttachmentService attachment;
	@PostMapping("/upload")
	public ResponseStructure<TaskAttachment> uploadAttachment(
            @RequestParam Long taskId,
            @RequestParam Long userId,
            @RequestParam MultipartFile file) {
		return attachment.uploadAttachment(taskId, userId, file);
		
	}
	@GetMapping("/task/{taskId}")
	public ResponseStructure<List<TaskAttachment>> getAttachments(
            @PathVariable Long taskId) {
		return attachment.getAllAttachment(taskId);
	}
	@GetMapping("/get/{attachmentId}")
	public ResponseStructure<TaskAttachment>  getAttachmentById(
            @PathVariable Long attachmentId) {
		return attachment.findAttachmentById(attachmentId);
	}
	
	@DeleteMapping("/delete")
	public ResponseStructure<TaskAttachment> deleteAttachment(
            @RequestParam Long attachmentId,
            @RequestParam Long userId) {
		return attachment.deleteAttachmentById(attachmentId, userId);
	}
	@PutMapping("/rename")	
	public ResponseStructure<TaskAttachment>  changeAttachmentName(
            @RequestParam Long attachmentId,
            @RequestParam String newName,
            @RequestParam Long updatedById) {
		return attachment.changeAttachmentName(attachmentId, newName, updatedById);
	}

	
	

}
