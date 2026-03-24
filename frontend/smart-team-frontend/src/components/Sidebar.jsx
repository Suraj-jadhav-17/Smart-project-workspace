import React from 'react'
import { Link } from 'react-router-dom'
import './Sidebar.css'

const Sidebar = () => {
  return (
   <div className='sidebar'>
      <h1>Menu</h1>
      <ul>
        <li><Link to="/">Dashboard</Link></li>
        <li><Link to="/projects">Projects</Link></li>
        <li><Link to="/teams">Teams</Link></li>
      </ul>
    </div>
  )
}

export default Sidebar