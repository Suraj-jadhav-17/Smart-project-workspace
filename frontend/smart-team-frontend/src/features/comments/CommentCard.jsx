import React from "react";
import "./CommentCard.css";

const CommentCard = ({ comment }) => {
  function timeAgo(date) {
    const seconds = Math.floor((new Date() - new Date(date)) / 1000);

    if (seconds < 60) return "just now";
    if (seconds < 3600) return Math.floor(seconds / 60) + "m ago";
    if (seconds < 86400) return Math.floor(seconds / 3600) + "h ago";
    return Math.floor(seconds / 86400) + "d ago";
  }

  return (
    <div className="comment-card">
      
      <div className="comment-header">
        <div className="c-left">
          <span className="c-user">{comment.author.userName}</span>
          <span className="action">commented on</span>
          <span className="c-task">{comment.task.title}</span>
        </div>

        <div className="c-right">
          <span className="time">{timeAgo(comment.createdAt)}</span>
        </div>
      </div>

      <div className="comment-meta">
        in <span className="c-project">{comment.task.project.name}</span>
      </div>

      <p className="comment-text">{comment.content}</p>
    </div>
  );
};

export default CommentCard;