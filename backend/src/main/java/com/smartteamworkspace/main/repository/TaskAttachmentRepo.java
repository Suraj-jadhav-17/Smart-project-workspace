package com.smartteamworkspace.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartteamworkspace.main.entity.TaskAttachment;

public interface TaskAttachmentRepo extends JpaRepository<TaskAttachment, Long>{
 List<TaskAttachment> findByTask_Id(Long taskID);
}
