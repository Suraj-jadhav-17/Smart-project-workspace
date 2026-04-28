
import React, { use, useEffect, useState } from 'react'
import api from '../../api/api';
import ProgressRing from './ProgressRing';
import './ProjectCard.css';
import project_logo from '../../assets/project_dark.png';
import team_logo from '../../assets/team_dark.png';
import public_logo from '../../assets/public.png';
import private_logo from '../../assets/private.png';
import inprogressIcon from '../../assets/in-progress.png';
import completedIcon from '../../assets/completed.png';
import delayedIcon from '../../assets/delayed.png';
import cancelledIcon from '../../assets/cancelled.png';
import startDayIcon from '../../assets/start_dayIcon.png'
import dueDateIcon from '../../assets/dueDateIcon.png'
import membersIcon from '../../assets/membersIcon.png'
const ProjectCard = ({project, varient}) => {

  const [tasks,setTasks]=useState([]);
  const[projectMembers,setProjectMembers]=useState([]);

  useEffect(()=>{
    const fetchdata=async()=>{
      try{
        const [taskres, memberres] = await Promise.all([
           api.get(`/tasks/project/${project.id}`),
           api.get(`/project-member/project/${project.id}`),
      
        ]);
        setTasks(taskres.data.data);
        setProjectMembers(memberres.data.data);
             console.log("tasks", taskres.data.data);
       console.log("members", memberres.data.data);
      }catch(err){
        console.error("Error fetching tasks for project:", err);
      }
    }
    fetchdata();
  },[])
  
  const completed = tasks.filter((task)=>task.status==="COMPLETED").length;
  const total = tasks.length;
  const percentage = total === 0 ? 0 : Math.round((completed / total) * 100);
  
 if(varient === "dashboard"){
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
 else{
    return(
      <div className="projects-card">
         <div className="projects-header">
          <h3 className="project-title"><img src={project_logo} alt="" />{project.name}</h3>
          <p className={project.visibility === "PUBLIC" ? "public" : "private"}>
            <img src={project.visibility === "PUBLIC" ? public_logo : private_logo} alt="" /> <span>{project.visibility}</span>
          </p>
        </div>
        <div className="projects-details">
           <div className="projects-status-team">
             <div className="project-staus">
             
                <img src={project.status === "ACTIVE"? inprogressIcon:project.status=== "COMPLETED" ? completedIcon : project.status === "DELAYED" ? delayedIcon : cancelledIcon} alt="" />
                 <p className={project.status === "ACTIVE" ? "active" : project.status === "COMPLETED" ? "completed" : project.status === "DELAYED" ? "delayed" : "cancelled"}>
                   {project.status}
                 </p>
            </div>
            <p className="projects-team"><img src={team_logo} alt="team:" />{project.team?.name || "No Team"}</p>
           </div>
            <div className="projects-dates">
               <p className="startDay">
                <img src={startDayIcon} alt="start:"/> {project.startDay}
               </p>
              <p className="dueDate"><img src={dueDateIcon} alt="Due:" />{project.deadline}</p>

             </div>
            <p className="members-count"><img src={membersIcon} alt="" />{projectMembers.length} members</p>
            <div className="projects-progress">
          <p>task completed:{completed}/{total}</p>
            <div className="progress-bar">
              
              <div className="progress-fill" style={{ width: `${percentage}%` }}>

              </div>
            </div>
              <p className="progress-text">Team Progress: {percentage}%</p>
            </div>
        </div>
      </div>
)
  
 }
}

export default ProjectCard
