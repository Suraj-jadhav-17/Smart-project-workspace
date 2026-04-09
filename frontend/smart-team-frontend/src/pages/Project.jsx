import React, { use, useEffect, useState } from 'react'
import api from '../api/api';
import ProjectCard from '../features/projects/ProjectCard';

const Project = () => {
  const userId = 1;
  const[projects,setProjects]=useState([]);
  const[loading,setLoading]=useState(true);
  useEffect(()=>{
      api.get(`projects/fetchAll/${userId}`).then((res)=>setProjects(res.data.data)).catch((err)=>console.error("Error fetching projects:", err)).finally(()=>setLoading(false));
  },[])
  return (
    <div>
      <h1>Projects</h1>
      {loading ? (
        <p>Loading...</p>
      ) : (
        
        <ul>
          {projects.map((project) => (
           <ProjectCard key={project.id} project={project}/>
          ))}
        </ul>
      )}
    </div>
  )
}

export default Project