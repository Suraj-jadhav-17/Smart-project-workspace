import React from 'react'
import './MainLayout.css'

import { Outlet } from 'react-router-dom'
import Navbar from './Navbar'
import Sidebar from '../layout/Sidebar'
const MainLayout = () => {
  return (
      <div className="app">
      
      <div className="navbar">
        <Navbar/>
      </div>


      <div className="main">
        
        
        <div className="sidebar">
         <Sidebar/>
        </div>

        <div className="content">
          <Outlet/>
        </div>

      </div>
    </div>
  )
}

export default MainLayout