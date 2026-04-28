import React, { use, useEffect, useState } from 'react'
import api from '../api/api';
import ProjectCard from '../features/projects/ProjectCard';
import './Project.css';
const Project = () => {
  const userId = 1;
  const[projects,setProjects]=useState([]);
  const[loading,setLoading]=useState(true);
  useEffect(()=>{
      api.get(`projects/fetchAll/${userId}`).then((res)=>setProjects(res.data.data)).catch((err)=>console.error("Error fetching projects:", err)).finally(()=>setLoading(false));
  },[])
  return (
    <div>
     
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="projects">

          <div className="project-stat">
           <div className="project-stat-card">
            <p>Total </p>
            <h2>{projects.length}</h2>
           </div>
           <div className="project-stat-card">
            <p>Ongoing </p>
            <h2>{projects.filter(project => project.status === 'ACTIVE').length}</h2>
           </div>
           <div className="project-stat-card">
            <p>Completed </p>
            <h2>{projects.filter(project => project.status === 'COMPLETED').length}</h2>
           </div>
           <div className="project-stat-card">
            <p>Delayed </p>
            <h2>{projects.filter(project => project.status === 'DELAYED').length}</h2>
           </div>

          </div>
          <div className="projects-nav">
            <div className="searchbar">
              <input type="text" placeholder='Search Projects...'/>
              <button>Search</button>
            </div>
            <div className="create-project">
              <button>Create Project</button>
            </div>
           <div className="dropdown-container">
                <select placeholder="Sort By">
              {/* <option value="" >Sort By</option> */}
              <option value="name">Name</option>
              <option value="deadline">Deadline</option>
              <option value="status">Status</option>
            </select>
            <select>
              <option value="all">All Projects</option>
              <option value="my">My Projects</option>
              <option value="public">Public Projects</option>
            </select>
            <select>
              <option value="allStatus">All </option> 
              <option value="active">Active</option>
              <option value="completed">Completed</option>
              <option value="delayed">Delayed</option>
            </select>
            
           </div>
          </div>
        <div className="projects-container">
        <div className="projects-cards">
          {projects.map((project) => (
           <ProjectCard key={project.id} project={project} varient="project" />
          ))}
        
        </div>
          
        </div>
        </div>
        
      )}
    </div>
  )
}

export default Project