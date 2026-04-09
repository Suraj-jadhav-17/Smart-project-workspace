import { BrowserRouter, Routes, Route, Navigate, redirect } from "react-router-dom";
import MainLayout from "./layout/MainLayout";


import Project from "./pages/Project";
import Teams from "./pages/Teams";
import ProjectDetails from "./pages/ProjectDetails";  
import DashBoard from "./pages/Dashboard";

function App() {
  return (
    <BrowserRouter>
      <Routes>
     
        <Route element={<MainLayout />}>
          <Route path="/" element={<DashBoard />} />
          <Route path="/dashboard" element={<Navigate to="/" />} />
          <Route path="/projects" element={<Project />} />
          <Route path="/teams" element={<Teams />} />
          <Route path="/projects/:id" element={<ProjectDetails />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;