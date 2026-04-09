import React, { useEffect, useState } from 'react'
import TestAPI from './TestApi'
import api from '../api/api';
import ProjectCard from '../features/projects/ProjectCard';
import './DashBoard.css';
import TaskCard from '../features/tasks/TaskCard';
import { getCommentsByTaskId } from '../features/projects/projectApi';
import CommentCard from '../features/comments/CommentCard';
import TeamCard from '../features/teams/TeamCard';

const DashBoard = () => {
  const[projects, setProjects] = useState([]);
  const[tasks,setTasks]=useState([]);
  const[loading,setLoading]=useState(true); 
  const [comments, setComments] = useState([]);
  const[teams,setTeams]=useState([]);
  const[teamMembers,setTeamMembers]=useState([]);
  const userId = 1;
  useEffect(()=>{
     Promise.all([
    api.get(`/projects/all/${userId}`),
    api.get(`/tasks/user/${userId}`),
    api.get(`/teams/user/${userId}`)

  ])
  .then(([projectsRes, tasksRes, teamsRes]) => {
    setProjects(projectsRes.data.data);
    setTasks(tasksRes.data.data);
    setTeams(teamsRes.data.data);
  }).catch((err)=>{
      console.error("Error fetching data:", err);
    }).finally(() => {
      setLoading(false);
    });
  },[]);

  useEffect(() => {
  if (tasks.length === 0) return;

  const fetchComments = async () => {
    try {
      const results = await Promise.all(
        tasks.map(task => getCommentsByTaskId(task.id))
      );

      const allComments = results.flat();
      const sorted = allComments.sort(
  (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
  );
   const latest = sorted.slice(0, 5);
      setComments(latest);
    } catch (err) {
      console.error(err);
    }
  };

  fetchComments();
}, [tasks]);
  const tasksByTeam = teams.map(team => {
  const myTasks = tasks.filter(t => {
    return (
      t.project?.team?.id === team.id &&
      t.assignee?.userId === userId
    );
  });

  return {
    ...team,
    myTaskCount: myTasks.length
  };
});
  return (
    <div className='dashboard-container'>
        
        <div className="stats-row">
          <div className="stat-card">
              <p>Total Projects</p>
              <h2>{projects.length}</h2>
          </div>
          <div className="stat-card">
              <p>Total Tasks</p>
              <h2>{tasks.length}</h2>
          </div>
          <div className="stat-card">
              <p>Completed Task</p>
              <h2>{tasks.filter(task => task.status === 'COMPLETED').length}</h2>
          </div>
          <div className="stat-card">
              <p>Delayed Task</p>
              <h2>{tasks.filter(task => task.status === 'DELAYED').length}</h2>
        </div>
    </div>
    <div className="container">
    
        
        <div className="tasks-list">
          <h2>Tasks</h2>
          {tasks.length === 0 ? ("No tasks found") : (
            
              <div className="task-content">
                {tasks.map(task => (
                  <TaskCard key={task.id} task={task} />
                  ))}
              </div>
            
          )}
        </div>
          <div className="project-container">
        <h2>Projects</h2>
        <div className="projects-grid">
          {projects.length === 0 ? (
            <p>No projects found</p>
          ) : (
            projects.map(project => <ProjectCard key={project.id} project={project}  />)
          )}
        </div>
      </div>
        <div className="teams">
          <h2>Teams</h2>
          {teams.length === 0 ? (
            <p>No teams found</p>
          ) : (
            <div className="teams-grid">
              {tasksByTeam.map(team => (
                <TeamCard key={team.id} team={team} userId={userId}  myTaskCount={team.myTaskCount} />
              ))}
            </div>
          )}

        </div>
        <div className="comment-container">
            <h2>Recent Comments</h2>
          
            {comments.length === 0 ? (
              <p>No comments found</p>
            ) : (
              comments.map(comment => (
                <CommentCard key={comment.id} comment={comment} />
              ))
            )}

        </div>
      
      </div>
    </div>
  )
}

export default DashBoard