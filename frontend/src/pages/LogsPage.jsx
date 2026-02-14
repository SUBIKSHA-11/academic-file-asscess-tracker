import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function LogsPage() {

  const [logs, setLogs] = useState([]);
  const [filter, setFilter] = useState("");

  useEffect(() => {
    fetchLogs();
  }, []);

  const fetchLogs = () => {
    api.get("/admin/logs")
      .then(res => setLogs(res.data));
  };

  const filteredLogs = logs.filter(log =>
    filter === "" || log.action === filter
  );

  return (
    <div>

      <h2>Activity Logs</h2>

      {/* Filter Dropdown */}
      <div style={{ marginBottom: "15px" }}>
        <select onChange={(e) => setFilter(e.target.value)}>
          <option value="">All</option>
          <option value="UPLOAD">UPLOAD</option>
          <option value="VIEW">VIEW</option>
          <option value="DOWNLOAD">DOWNLOAD</option>
          <option value="DELETE">DELETE</option>
        </select>
      </div>

      <table border="1" width="100%">
        <thead>
          <tr>
            <th>User</th>
            <th>File</th>
            <th>Action</th>
            <th>IP Address</th>
            <th>Time</th>
          </tr>
        </thead>

        <tbody>
          {filteredLogs.map(log => (
            <tr key={log.id}>
              <td>{log.user?.email}</td>
              <td>{log.file?.fileName}</td>
              <td>{log.action}</td>
              <td>{log.ipAddress}</td>
              <td>{log.timestamp}</td>
            </tr>
          ))}
        </tbody>
      </table>

    </div>
  );
}
