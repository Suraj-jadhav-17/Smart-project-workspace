package com.smartteamworkspace.main.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.smartteamworkspace.main.entity.TaskAttachment;
import com.smartteamworkspace.main.response.ResponseStructure;

public interface TaskAttachmentService {

	ResponseStructure<TaskAttachment> uploadAttachment(Long taskId,Long userID,MultipartFile file);
	
	
	ResponseStructure<List<TaskAttachment>> getAllAttachment(Long taskId);
	
	ResponseStructure<TaskAttachment> findAttachmentById(Long id);
	
	ResponseStructure<TaskAttachment> deleteAttachmentById(Long attachmentID,Long userId);
	
	ResponseStructure<TaskAttachment> changeAttachmentName( Long attachMentId, String newName,Long updatedById);
}
