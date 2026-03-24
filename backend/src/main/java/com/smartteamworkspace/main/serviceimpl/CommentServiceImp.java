package com.smartteamworkspace.main.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartteamworkspace.main.entity.AppUser;
import com.smartteamworkspace.main.entity.Comment;
import com.smartteamworkspace.main.entity.Task;
import com.smartteamworkspace.main.exception.AccessDeniedException;
import com.smartteamworkspace.main.exception.InvalidRequestException;
import com.smartteamworkspace.main.exception.ResourceNotFoundException;
import com.smartteamworkspace.main.repository.AppUserRepo;
import com.smartteamworkspace.main.repository.CommentRepo;
import com.smartteamworkspace.main.repository.TaskRepo;
import com.smartteamworkspace.main.response.ResponseStructure;
import com.smartteamworkspace.main.service.CommentService;

@Service
public class CommentServiceImp implements CommentService{
	@Autowired
   private CommentRepo commentRepo;
	@Autowired
   private TaskRepo taskRepo;
	@Autowired
	private AppUserRepo userRepo;
	private ResponseStructure<Comment> createResponse(Comment data,int statusCode,String message){
		ResponseStructure<Comment> response=new ResponseStructure<>();
		response.setData(data);
		response.setStatusCode(statusCode);
		response.setMessage(message);
		return response;
	}
	@Override
	public ResponseStructure<Comment> addComment(Long taskID, Long userID, String content) {
		  Optional<Task> taskOptional=taskRepo.findById(taskID);
		  if(!taskOptional.isPresent()) {
			  throw new ResourceNotFoundException("Task not found");
		  }
		 Optional<AppUser> userOptional= userRepo.findById(userID);
		 if(!userOptional.isPresent()) {
			 throw new ResourceNotFoundException("User not found");
		 }
		 if(content==null||content.isBlank()) {
			 throw new InvalidRequestException("Comment content cannot be empty");
		 }
		 Comment comment = new Comment();
		 comment.setContent(content);
		 comment.setTask(taskOptional.get());
		 comment.setAuthor(userOptional.get());
		 Comment addedComment= commentRepo.save(comment);
		 

		return createResponse(addedComment, 201,"Comment added");
	}

	@Override
	public ResponseStructure<Comment> modifyComment(Long commentID, Long userID, String newContent) {
	    Optional<Comment> commentOptional = commentRepo.findById(commentID);
	    if(!commentOptional.isPresent()) {
	    	throw new ResourceNotFoundException("Comment not found");
	    }
	    
	    Optional<AppUser> userOptional= userRepo.findById(userID);
	    
	    if(!userOptional.isPresent()) {
	    	throw new ResourceNotFoundException("User not found");
	    }
	    Comment comment=commentOptional.get();//comment 
	    if(!comment.getAuthor().getUserId().equals(userID)) {
	    	throw new AccessDeniedException("You cannot modify this comment");
	    }
	    if(newContent==null||newContent.isBlank()) {
	    	throw new InvalidRequestException("Comment content cannot be empty");
		 }
	    comment.setContent(newContent);
	    Comment modified= commentRepo.save(comment);
	    
	    
		return createResponse(modified, 200, "Comment modified");
	}

	@Override
	public ResponseStructure<List<Comment>> getAllComment(Long taskId) {
		Optional<Task> taskOptional= taskRepo.findById(taskId);
		if(!taskOptional.isPresent()) {
			throw new ResourceNotFoundException("Task not found");
		}
		
		List<Comment> comments= commentRepo.findByTask_Id(taskId);
		ResponseStructure<List<Comment>> response= new  ResponseStructure<>();
		response.setData(comments);
		response.setStatusCode(200);
		response.setMessage(comments.isEmpty()? "No comment":"Comments fetched");
		return response;
	}

	@Override
	public ResponseStructure<Comment> getCommentById(Long commentId) {
		Optional<Comment> commentOptional= commentRepo.findById(commentId);
		if(!commentOptional.isPresent()) {
			throw new ResourceNotFoundException("Comment not found");
		}
		Comment comment = commentOptional.get();
		return createResponse(comment, 200, "Comment fetched");
	}

	@Override
	public ResponseStructure<Comment> deleteById(Long commentId, Long userId) {
		 Optional<Comment> commentOptinal = commentRepo.findById(commentId);
		 if(!commentOptinal.isPresent()) {
			 throw new ResourceNotFoundException("Comment not found");
		 }
		 Comment comment=commentOptinal.get();
		 if(!comment.getAuthor().getUserId().equals(userId)) {
			  throw new AccessDeniedException("You cannot delete this comment");
		 }
		commentRepo.delete(comment);
		
		return createResponse(comment, 200, "Comment deleted");
	}

}
