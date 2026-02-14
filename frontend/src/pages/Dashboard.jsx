import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function Dashboard() {

  const [stats, setStats] = useState({});

  useEffect(() => {
    api.get("/admin/dashboard/stats")
       .then(res => setStats(res.data));
  }, []);

  return (
    <div style={{ padding: "20px" }}>
      <h2>Admin Dashboard</h2>

      <p>Total Users: {stats.totalUsers}</p>
      <p>Faculty: {stats.totalFaculty}</p>
      <p>Students: {stats.totalStudents}</p>
    </div>
  );
}
