import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function AlertsPage() {

  const [alerts, setAlerts] = useState([]);

  useEffect(() => {
    fetchAlerts();
  }, []);

  const fetchAlerts = () => {
    api.get("/admin/alerts")
      .then(res => setAlerts(res.data));
  };

  const resolveAlert = async (id) => {
    await api.put(`/admin/alerts/${id}/resolve`);
    fetchAlerts();
  };

  return (
    <div>

      <h2>Security Alerts</h2>

      <table border="1" width="100%">
        <thead>
          <tr>
            <th>User</th>
            <th>Reason</th>
            <th>Severity</th>
            <th>Status</th>
            <th>Created</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {alerts.map(alert => (
            <tr key={alert.id}>
              <td>{alert.user?.email}</td>
              <td>{alert.reason}</td>
              <td style={{
                color:
                  alert.severity === "HIGH" ? "red" :
                  alert.severity === "MEDIUM" ? "orange" : "green"
              }}>
                {alert.severity}
              </td>
              <td>{alert.status}</td>
              <td>{alert.createdAt}</td>
              <td>
                {alert.status === "OPEN" &&
                  <button onClick={() => resolveAlert(alert.id)}>
                    Resolve
                  </button>}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

    </div>
  );
}
