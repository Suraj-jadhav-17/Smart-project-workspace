import React from 'react'
import './TaskCard.css'
import dueDate from'../../assets/due-date-dark.png'
import priority_logo from'../../assets/priority.png'
import task_icon from'../../assets/task.png'
import project_logo from'../../assets/project_dark.png'
import todo_logo from'../../assets/to-do.png'
import completed_logo from'../../assets/completed.png'
import delayed_logo from'../../assets/delayed.png'
import inProgress_logo from'../../assets/work-in-progress.png'
const TaskCard = ({ task }) => {
  return (
    <div className='task'>
        <div className="header">
           <div className="tasktitle">
            <img src={task_icon} alt="Task Icon" />
            <p className='title'>{task.title}</p>
           </div>
          
            <p className='project'> <img src={project_logo} alt="" />{task.project.name}</p>
         
             
        </div>
       <div className="information">
         <p><img src={dueDate} alt="Due Date" /> {task.dueDate}</p>
        <div className='priority'>
          <img src={priority_logo} alt="Priority" />
          <p className={`priority-${task.priority.toLowerCase()}`}> {task.priority}</p>
        </div>
        <div className="status">
          <img src={task.status==="TODO"?todo_logo:task.status==="IN_PROGRESS"?inProgress_logo:task.status==="COMPLETED"?completed_logo:delayed_logo} alt="" />
              <p className={`status-${task.status.toLowerCase()}`}> {task.status}</p>
        </div>
    
       
       </div>
        
    </div>
  )
}

export default TaskCard