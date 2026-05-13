import React, { useState } from 'react'
import './MainLayout.css'

import { Outlet } from 'react-router-dom'
import Navbar from './Navbar'
import Sidebar from '../layout/Sidebar'
import api from '../api/api'
const MainLayout = () => {
  const userId=1;
  const[user,setUser]=useState({});
  // const useEffect(() => {
  //      api.get(`/user/${userId}`).then((res) => {
  //        setUser(res.data.data);
  //      });
  // }, []);
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
          <Outlet user={user} />
        </div>

      </div>
    </div>
  )
}

export default MainLayout