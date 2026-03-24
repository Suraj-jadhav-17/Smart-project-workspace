package com.smartteamworkspace.main.serviceimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartteamworkspace.main.entity.AppUser;
import com.smartteamworkspace.main.entity.Task;
import com.smartteamworkspace.main.entity.TaskAttachment;
import com.smartteamworkspace.main.exception.AccessDeniedException;
import com.smartteamworkspace.main.exception.FileStorageException;
import com.smartteamworkspace.main.exception.InvalidRequestException;
import com.smartteamworkspace.main.exception.ResourceNotFoundException;
import com.smartteamworkspace.main.repository.AppUserRepo;
import com.smartteamworkspace.main.repository.TaskAttachmentRepo;
import com.smartteamworkspace.main.repository.TaskRepo;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.TaskAttachmentService;

import jakarta.transaction.Transactional;

@Service
public class TaskAttachmentServiceImp  implements TaskAttachmentService{
	
    @Autowired
	private TaskRepo taskRepo;
	
    @Autowired
	private TaskAttachmentRepo attachmentRepo;
	
    @Autowired
	private AppUserRepo userRepo;
	
    private final Path uploadDir = Paths.get("uploads/tasks");
	 
	private ResponseStructure<TaskAttachment> createResponse(TaskAttachment data, int statusCode,String massage){
		ResponseStructure<TaskAttachment> response = new ResponseStructure<>();
		response.setData(data);
		response.setStatusCode(statusCode);
		response.setMessage(massage);
		return response;
	}
    @Transactional
	@Override
	public ResponseStructure<TaskAttachment> uploadAttachment(Long taskId, Long userId, MultipartFile file) {
		Task task= taskRepo.findById(taskId)
				 	.orElseThrow(()->new ResourceNotFoundException("Task not found"));
		AppUser user =userRepo.findById(userId)
						.orElseThrow(()->new ResourceNotFoundException("User not found"));
				if(!task.getAssignee().getUserId().equals(userId)) {
					throw new AccessDeniedException("Only task assignee can upload attachment");
				}
				if(file.isEmpty()) {
					throw new InvalidRequestException("File cannot be empty");
				}
				String fileName = System.currentTimeMillis() + "_" +file.getOriginalFilename().replaceAll("\\s+", "_");
				Path path = uploadDir.resolve(fileName);
			try {
				Files.createDirectories(path.getParent());
				Files.write(path, file.getBytes());
			}catch(IOException e) {
				throw new FileStorageException("File upload failed");
			}
		TaskAttachment attachment = new TaskAttachment();
		attachment.setFileName(fileName);
		attachment.setFileUrl(path.toString());
		attachment.setFileType(file.getContentType());
		attachment.setTask(task);
		attachment.setUploadedBy(user);
		
		TaskAttachment saved = attachmentRepo.save(attachment);
		
		return createResponse(saved, 201, "Attachment uploaded");
	}

	@Override
	public ResponseStructure<List<TaskAttachment>> getAllAttachment(Long taskId) {
		taskRepo.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
		List<TaskAttachment> attachments= attachmentRepo.findByTask_Id(taskId);
		
		ResponseStructure<List<TaskAttachment>> response= new ResponseStructure<>();
		response.setData(attachments);
		response.setStatusCode(200);
		response.setMessage(attachments.isEmpty()? "No attchment found": "Attachments fetched");
		return response;
	}

	@Override
	public ResponseStructure<TaskAttachment> findAttachmentById(Long id) {
		TaskAttachment attachment =attachmentRepo.findById(id)
				   					.orElseThrow(()-> new ResourceNotFoundException("Attachment not found"));
		
		return createResponse(attachment, 200, "Attachment found");
	}

	@Transactional
	@Override
	public ResponseStructure<TaskAttachment> deleteAttachmentById(Long attachmentID, Long userId) {
		TaskAttachment attachment = attachmentRepo.findById(attachmentID)
				                      .orElseThrow(()->new ResourceNotFoundException("Attachment not found"));
				if(!attachment.getUploadedBy().getUserId().equals(userId)) {
					throw new AccessDeniedException("You are not authorized to delete this attachment");
				}
		try {
			Files.deleteIfExists(Paths.get(attachment.getFileUrl()));
		}catch(IOException e){
			throw new FileStorageException("Failed to delete file");
		}	
		attachmentRepo.delete(attachment);
		
		return createResponse(attachment, 200, "Attachment deleted");
	}
	@Override
	public ResponseStructure<TaskAttachment> changeAttachmentName(Long attachMentId, String newName, Long updatedById) {
		TaskAttachment attachment = attachmentRepo.findById(attachMentId)
										.orElseThrow(()->new ResourceNotFoundException("Attachment not found"));
				if(!attachment.getUploadedBy().getUserId().equals(updatedById)) {
					throw new AccessDeniedException("You are not authorized to rename attachment");
				}
				if(newName==null|| newName.isBlank()) {
					throw new InvalidRequestException("Attachment name cannot be empty");
				}
		attachment.setFileName(newName);
		TaskAttachment updated= attachmentRepo.save(attachment);
		
		return createResponse(updated, 200, "Attachment name updated");
	}
	
	
}
