import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "./layout/MainLayout";

import Dashboard from "./pages/Dashboard";
import Project from "./pages/Project";
import Teams from "./pages/Teams";
import ProjectDetails from "./pages/ProjectDetailes";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Main App Layout */}
        <Route element={<MainLayout />}>
          <Route path="/" element={<Navigate to="/dashboard" />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/projects" element={<Project />} />
          <Route path="/teams" element={<Teams />} />
          <Route path="/projects/:id" element={<ProjectDetails />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;