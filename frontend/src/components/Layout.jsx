import { Link, Outlet } from "react-router-dom";

export default function Layout() {
  return (
    <div>

      <h2>Admin Panel</h2>

      <nav style={{ marginBottom: "20px" }}>
        <Link to="/admin/dashboard">Dashboard</Link> |{" "}
        <Link to="/admin/faculty">Faculty</Link> |{" "}
        <Link to="/admin/students">Students</Link> |{" "}
        <Link to="/admin/academic">Files</Link> |{" "}
        <Link to="/admin/alerts">Alerts</Link> |{" "}
        <Link to="/admin/logs">Logs</Link>
      </nav>

      <Outlet />

    </div>
  );
}
