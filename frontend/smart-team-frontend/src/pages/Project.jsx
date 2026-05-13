import React, { use, useEffect, useState } from 'react'
import api from '../api/api';
import ProjectCard from '../features/projects/ProjectCard';
import './Project.css';
import searchIcon from '../assets/searchIcon.png'
const Project = () => {
  const userId = 1;
  const[projects,setProjects]=useState([]);
  const[loading,setLoading]=useState(true);
  const[sortBy,setSortBy]=useState("createdAt");
  const[filterBy,setFilterBy]=useState("all");
  const[statusFilter,setStatusFilter]=useState("allStatus");
  const[userProjects,setUserProjects]=useState([]);
  const sortedProjects = [...projects].sort((a, b) => {
    if (sortBy === "createdAt") {
      return new Date(b.createdAt) - new Date(a.createdAt);
    } else if (sortBy === "name") {
      return a.name.localeCompare(b.name);
    } else if (sortBy === "deadline") {
      return new Date(a.deadline) - new Date(b.deadline);
    } else if (sortBy === "status") {
      return a.status.localeCompare(b.status);
    } else {
      return 0;
    } });
    
  const filteredProjects = sortedProjects.filter(project => {
    if (filterBy === "my") {
      return userProjects
    }else if (filterBy === "public") {
      return project.visibility === "PUBLIC";
    }
    return true;
  });
  const finalFilteredProjects = filteredProjects.filter(project => {
    if (statusFilter === "active") {
      return project.status === "ACTIVE";
    } else if (statusFilter === "completed") {
      return project.status === "COMPLETED";
    } else if (statusFilter === "delayed") {
      return project.status === "DELAYED";
    }
    return true;
  });

  useEffect(() => {

  Promise.all([
    api.get(`projects/fetchAll/${userId}`),
    api.get(`projects/all/${userId}`)
  ])
    .then(([projRes, userProjRes]) => {

      setProjects(projRes.data.data);
      setUserProjects(userProjRes.data.data);

    })
    .catch((err) =>
      console.error("Error fetching projects:", err)
    )
    .finally(() => setLoading(false));

}, []);
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
              <button><img src={searchIcon} alt="search" /></button>
            </div>
            <div className="create-project">
              
              <button>Create Project</button>
            </div>
           <div className="dropdown-container">
                <select value={sortBy} onChange={(e) => {console.log(e.target.value);setSortBy(e.target.value);
  }}
>
               <option value="createdAt">Created At</option>
              <option value="name" >Name</option>
              <option value="deadline">Deadline</option>
              <option value="status">Status</option>
            </select>
            <select value={filterBy} onChange={(e) => setFilterBy(e.target.value)}>
              <option value="all">All Projects</option>
              <option value="my">My Projects</option>
              <option value="public">Public Projects</option>
            </select>
            <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
              <option value="allStatus">All </option> 
              <option value="active">Active</option>
              <option value="completed">Completed</option>
              <option value="delayed">Delayed</option>
            </select>
            
           </div>
          </div>
        <div className="projects-container">
        <div className="projects-cards">
          {finalFilteredProjects.length > 0 ? (
            finalFilteredProjects.map((project) => (
              <ProjectCard key={project.id} project={project} varient="project" />
            ))
          ) : (
            <p>No projects found.</p>
          )}
        </div>
          
        </div>
        </div>
        
      )}
    </div>
  )
}

export default Project