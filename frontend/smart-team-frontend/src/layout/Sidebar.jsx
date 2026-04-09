import React from 'react'
import { NavLink } from 'react-router-dom'
import './Sidebar.css'
import team_logo from '../assets/group.png'
import dashboard_logo_light from '../assets/dashboard_light.png'
import dashboard_logo_dark from '../assets/dashboard_dark.png'
import project_logo_light from '../assets/project_light.png'
import project_logo_dark from '../assets/project_dark.png'
import team_logo_light from '../assets/team_light.png'
import team_logo_dark from '../assets/team_dark.png'
const Sidebar = () => {
  return (
   <div className='sidebar'>
    
      <ul>
  <li>
  <NavLink to="/" end>
    {({ isActive }) => (
      <>
        <img
          src={isActive ? dashboard_logo_light: dashboard_logo_dark}
          alt=""
        />
        <span className={isActive ? "active" : ""}>Dashboard</span>
      </>
    )}
  </NavLink>
</li>
<li>
  <NavLink to="/projects" >
  {({isActive})=>(<>
    <img src={isActive ? project_logo_light: project_logo_dark} alt="" />
    <span className={isActive ? "active" : ""}>Projects</span>
  </>)}
   
  </NavLink>
</li>
<li>
  <NavLink to="/teams" >
  {({isActive})=>(<>
    <img src={isActive ? team_logo_light: team_logo_dark} alt="" />
    <span className={isActive ? "active" : ""}>Teams</span>
  </>)} 

  </NavLink>
</li>
      </ul>
    </div>
  )
}

export default Sidebar