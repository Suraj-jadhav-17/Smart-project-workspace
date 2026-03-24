import React, { useEffect, useState } from 'react'
import TestAPI from './TestApi'
import api from '../api/api';
import ProjectCard from '../components/ProjectCard';

const DashBoard = () => {
  const[projects, setProjects] = useState([]);
  const[tasks,setTasks]=useState([]);
  const[loading,setLoading]=useState(true); 
  useEffect(()=>{
    const userId = 1;
    Promise.all([
      api.get(`/projects/all/${userId}`).then((res)=>setProjects(res.data.data)),
      api.get(`/tasks/user/${userId}`).then((res)=>setTasks(res.data.data))
    ]).catch((err)=>{
      console.error("Error fetching data:", err);
    }).finally(() => {
      setLoading(false);
    });
  },[]);
  return (
    <div>
      <div><h1>DashBoard</h1></div>
       <div style={{ display: "flex", gap: "30px",flexDirection:"column" }}>
        
        
        <div  style={{display:"block"}}><p>Total Projects: {projects.length}</p>
        <p>Total Tasks: {tasks.length}</p></div>
        <div style={{display:"flex",gap:"60px"}}>
        {projects.length===0  ? <p>No projects found</p>:projects.map((project) => (
          <ProjectCard key={project.id} project={project} />
        ))}
        </div>
      </div>
    </div>
  )
}

export default DashBoard