import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import {
  BarChart, Bar, XAxis, YAxis, Tooltip,
  PieChart, Pie, Cell, Legend,
  ResponsiveContainer
} from "recharts";

export default function Dashboard() {

  const [stats, setStats] = useState({});
  const [topFiles, setTopFiles] = useState([]);
  const [categoryStats, setCategoryStats] = useState([]);
  const [deptStats, setDeptStats] = useState([]);

 useEffect(() => {

  api.get("/admin/dashboard/stats")
    .then(res => setStats(res.data))
    .catch(err => console.log("Stats error:", err));

  api.get("/admin/analytics/top-files")
    .then(res => {
      const formatted = res.data.map(f => ({
        name: f.fileName,
        downloads: f.downloads
      }));
      setTopFiles(formatted);
    })
    .catch(err => console.log("Top files error:", err));

  api.get("/admin/analytics/category-stats")
    .then(res => {
      const formatted = Object.entries(res.data).map(([key, value]) => ({
        name: key,
        value: value
      }));
      setCategoryStats(formatted);
    })
    .catch(err => console.log("Category error:", err));

  api.get("/admin/analytics/department-stats")
    .then(res => {
      const formatted = Object.entries(res.data).map(([key, value]) => ({
        name: key,
        files: value
      }));
      setDeptStats(formatted);
    })
    .catch(err => console.log("Department error:", err));

}, []);


  const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042"];

  return (
    <div>

      <h2>Admin Dashboard</h2>

      {/* Stats Cards */}
      <div style={{ display: "flex", gap: "20px", marginBottom: "30px" }}>
        <div className="card">Users: {stats.totalUsers}</div>
        <div className="card">Faculty: {stats.totalFaculty}</div>
        <div className="card">Students: {stats.totalStudents}</div>
      </div>

      {/* Top Files Bar Chart */}
      <h3>Top Downloaded Files</h3>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={topFiles}>
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="downloads" fill="#8884d8" />
        </BarChart>
      </ResponsiveContainer>

      {/* Category Pie Chart */}
      <h3>Category Distribution</h3>
      <ResponsiveContainer width="100%" height={300}>
        <PieChart>
          <Pie
            data={categoryStats}
            dataKey="value"
            nameKey="name"
            outerRadius={100}
            label
          >
            {categoryStats.map((entry, index) => (
              <Cell key={index}
                fill={COLORS[index % COLORS.length]} />
            ))}
          </Pie>
          <Legend />
          <Tooltip />
        </PieChart>
      </ResponsiveContainer>

      {/* Department Bar Chart */}
      <h3>Department File Count</h3>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={deptStats}>
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="files" fill="#82ca9d" />
        </BarChart>
      </ResponsiveContainer>

    </div>
  );
}
