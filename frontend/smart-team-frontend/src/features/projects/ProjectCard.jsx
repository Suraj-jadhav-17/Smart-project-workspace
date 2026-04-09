
import React, { useEffect, useState } from 'react'
import api from '../../api/api';
import ProgressRing from './ProgressRing';
import './ProjectCard.css';
import project_logo from '../../assets/project_dark.png';
import team_logo from '../../assets/team_dark.png';
import public_logo from '../../assets/public.png';
import private_logo from '../../assets/private.png';
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


      <div className="p-title">
        <h3 className="project-title"><img src={project_logo} alt="" />{project.name}</h3>
      </div>
      <div className="details">
        <div className="project-details">
          <p className="project-meta">
            <img src={team_logo} alt="" /> <span>{project.team?.name || "No Team"}</span>
          </p>
          <p className={project.visibility === "PUBLIC" ? "public" : "private"}>
            <img src={project.visibility === "PUBLIC" ? public_logo : private_logo} alt="" /> <span>{project.visibility}</span>
          </p>
        </div>
        <div className="progress-details">
           <ProgressRing percentage={percentage} />

       
       <p className={`task-info ${total === 0 ? "no-task" : ""}`}>
          {total === 0
            ? "No tasks yet"
            : `${completed}/${total} tasks`}
        </p>
        </div>
      </div>

    </div>
  )
}

export default ProjectCard
