package com.smartteamworkspace.main.service;

import java.util.List;

import com.smartteamworkspace.main.entity.Comment;
import com.smartteamworkspace.main.response.ResponseStructure;

public interface CommentService {
	
	ResponseStructure<Comment> addComment(Long taskID,Long userID, String content);
	
	ResponseStructure<Comment> modifyComment(Long commentID,Long userID,String newContent);
	
	ResponseStructure<List<Comment>> getAllComment(Long taskId);
	
	ResponseStructure<Comment> getCommentById(Long id);
	
	ResponseStructure<Comment> deleteById(Long id,Long userId);
	

}
