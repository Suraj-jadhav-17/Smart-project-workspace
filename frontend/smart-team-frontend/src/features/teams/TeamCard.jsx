import React, { use, useEffect, useState } from 'react'
import api from '../../api/api';
import './TeamCard.css'
const TeamCard = ({ team, userId ,myTaskCount}) => {
    const [members, setMembers]= useState([]);
    const [teamTasks, setTeamTasks] = useState([]);
    useEffect(() => {
        api.get(`/team-members/team/${team.id}`)
        .then(res => {
            setMembers(res.data.data);  
        });
    }, [team.id]);
   
    useEffect(() => {
        api.get(`/tasks/team/${team.id}`)
        .then(res => {
            setTeamTasks(res.data.data);  
        });
}, [team.id]);

const completed = teamTasks.filter(t => t.status === 'COMPLETED').length;
const inprogress = teamTasks.filter(t => t.status === 'IN_PROGRESS').length;
const delayed = teamTasks.filter(t => t.status === 'DELAYED').length; 
const total = teamTasks.length; 
const progress = total === 0 ? 0 : Math.round((completed / total) * 100); 
const userMembers = members.filter(
  m => m.user.userId === userId
);
const myMember = members.find(m => m.user.userId === userId);
 console.log('User Members:', userMembers);
  return (
    <div className='team-card'>
      <div className="team-header">
        <h3>{team.name}</h3>
        <span className="team-role">
  {myMember?.designation}
</span>
      </div>
      <div className="progress-bar">
        <div className="progress-fill" style={{ width: `${progress}%` }}>

        </div>
      </div>
      <p className="progress-text">Team Progress: {progress}%</p>

      <div className="task-stats">
        <span>Completed: {completed}</span>
        <span>In Progress: {inprogress}</span>
        <span>Delayed: {delayed}</span>
      </div>
    </div>
  )
}

export default TeamCard