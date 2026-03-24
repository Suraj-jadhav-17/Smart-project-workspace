package com.smartteamworkspace.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartteamworkspace.main.entity.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long>{

	List<Comment> findByTask_Id(Long taskId);
}
