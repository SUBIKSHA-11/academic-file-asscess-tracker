import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import FacultyPage from "./pages/FacultyPage";
import StudentPage from "./pages/StudentPage";
import AlertsPage from "./pages/AlertsPage";
import LogsPage from "./pages/LogsPage";
import Layout from "./components/Layout";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* Login Routes */}
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />

        {/* Admin Routes */}
        <Route path="/admin" element={<Layout />}>

          <Route index element={<Dashboard />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="faculty" element={<FacultyPage />} />
          <Route path="students" element={<StudentPage />} />
          <Route path="alerts" element={<AlertsPage />} />
          <Route path="logs" element={<LogsPage />} />

        </Route>

      </Routes>
    </BrowserRouter>
  );
}

export default App;
