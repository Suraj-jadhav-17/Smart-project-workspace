import React, { useEffect, useState } from 'react'
import api from '../api/api';
import ProgressRing from './ProgressRing';
import './ProjectCard.css';
const ProjectCard = ({project}) => {
  const [tasks,setTasks]=useState([]);
  useEffect(()=>{
    api.get(`/tasks/project/${project.id}`)
     .then((res)=>setTasks(res.data.data))
     .catch((err)=>{
      console.error("Error fetching tasks for project:", err);
     })
  },[project.id])
  const completed = tasks.filter((task)=>task.status==="COMPLETED").length;
  const total = tasks.length;
  const percentage = total === 0 ? 0 : Math.round((completed / total) * 100);

  return (
   <div className='projectCard'>
  <div className="details">
    <h3>{project.name}</h3>
    <p>Team: {project.team?.name || "No Team"}</p>
    <p>Visibility: {project.visibility}</p>
  </div>

 <div className="progress">
  <ProgressRing percentage={percentage} />

  <p className="task-info">
    {total === 0 
      ? "No tasks yet" 
      : `${completed}/${total} tasks`}
  </p>
</div>
</div>
  )
}

export default ProjectCard