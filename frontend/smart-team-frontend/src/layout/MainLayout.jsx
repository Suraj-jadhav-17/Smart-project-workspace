import React from 'react'
import './MainLayout.css'
import Navbar from '../components/Navbar'
import Sidebar from '../components/Sidebar'

const MainLayout = ({ children }) => {
  return (
      <div className="app">
      {/* Navbar */}
      <div className="navbar">
        <h2>Smart Workspace</h2>
      </div>

      {/* Main Section */}
      <div className="main">
        
        {/* Sidebar */}
        <div className="sidebar">
          <h3>Menu</h3>
          <ul>
            <li><a href="#">Dashboard</a></li>
            <li><a href="#">Projects</a></li>
            <li><a href="#">Teams</a></li>
          </ul>
        </div>

        {/* Content */}
        <div className="content">
          {children}
        </div>

      </div>
    </div>
  )
}

export default MainLayout