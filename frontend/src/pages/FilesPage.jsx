import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import api from "../api/axiosConfig";

export default function FilesPage() {

  const [files, setFiles] = useState([]);
  const [searchParams] = useSearchParams();
  const categoryParam = searchParams.get("category");

  const [fileData, setFileData] = useState({
    department: "",
    semester: "",
    subject: "",
    category: categoryParam || "NOTES",
    sensitivity: "PUBLIC"
  });

  const [selectedFile, setSelectedFile] = useState(null);

  // ===============================
  // Fetch Files
  // ===============================
  const fetchFiles = async () => {
    try {
      if (categoryParam) {
        const res = await api.get(`/files/category/${categoryParam}`);
        setFiles(res.data);
      } else {
        const res = await api.get("/files");
        setFiles(res.data);
      }
    } catch (err) {
      console.error(err);
      alert("Failed to load files");
    }
  };

  useEffect(() => {
    fetchFiles();
  }, [categoryParam]);

  // ===============================
  // Upload File
  // ===============================
  const handleUpload = async (e) => {
    e.preventDefault();

    if (!selectedFile) {
      alert("Please select a file");
      return;
    }

    const formData = new FormData();
    formData.append("file", selectedFile);
    formData.append("department", fileData.department);
    formData.append("semester", fileData.semester);
    formData.append("subject", fileData.subject);
    formData.append("category", fileData.category);
    formData.append("sensitivity", fileData.sensitivity);

    try {
      await api.post("/files/upload", formData);
      alert("File uploaded successfully ✅");
      setSelectedFile(null);
      fetchFiles();
    } catch (err) {
      alert(err.response?.data || "Upload failed ❌");
    }
  };

  // ===============================
  // Delete
  // ===============================
  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure?")) return;

    try {
      await api.delete(`/files/${id}`);
      fetchFiles();
    } catch (err) {
      alert("Delete failed");
    }
  };

  // ===============================
  // Download
  // ===============================
  const handleDownload = (id) => {
    window.open(`http://localhost:8080/files/${id}/download`);
  };

  // ===============================
  // View
  // ===============================
  const handleView = (id) => {
    window.open(`http://localhost:8080/files/${id}/view`);
  };

  return (
    <div style={{ padding: "30px" }}>

      <h2>Files Module</h2>

      {categoryParam && (
        <h3>Showing Category: {categoryParam}</h3>
      )}

      {/* ================= UPLOAD FORM ================= */}
      <form onSubmit={handleUpload} style={{ marginBottom: "30px" }}>

        <input
          placeholder="Department"
          required
          onChange={e =>
            setFileData({ ...fileData, department: e.target.value })
          }
        />

        <input
          placeholder="Semester"
          required
          onChange={e =>
            setFileData({ ...fileData, semester: e.target.value })
          }
        />

        <input
          placeholder="Subject"
          required
          onChange={e =>
            setFileData({ ...fileData, subject: e.target.value })
          }
        />

        <select
          value={fileData.category}
          onChange={e =>
            setFileData({ ...fileData, category: e.target.value })
          }
        >
          <option value="NOTES">NOTES</option>
          <option value="QUESTION_PAPER">QUESTION_PAPER</option>
          <option value="MARKSHEET">MARKSHEET</option>
          <option value="ASSIGNMENT">ASSIGNMENT</option>
          <option value="LAB">LAB</option>
          <option value="OTHER">OTHER</option>
        </select>

        <select
          onChange={e =>
            setFileData({ ...fileData, sensitivity: e.target.value })
          }
        >
          <option value="PUBLIC">PUBLIC</option>
          <option value="INTERNAL">INTERNAL</option>
          <option value="CONFIDENTIAL">CONFIDENTIAL</option>
        </select>

        <input
          type="file"
          onChange={e => setSelectedFile(e.target.files[0])}
        />

        <button type="submit">Upload</button>
      </form>

      {/* ================= FILE TABLE ================= */}
      <table border="1" width="100%">
        <thead>
          <tr>
            <th>Name</th>
            <th>Department</th>
            <th>Semester</th>
            <th>Category</th>
            <th>Sensitivity</th>
            <th>Downloads</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {files.length === 0 ? (
            <tr>
              <td colSpan="7" style={{ textAlign: "center" }}>
                No files found
              </td>
            </tr>
          ) : (
            files.map(file => (
              <tr key={file.id}>
                <td>{file.fileName}</td>
                <td>{file.department}</td>
                <td>{file.semester}</td>
                <td>{file.category}</td>
                <td>{file.sensitivity}</td>
                <td>{file.downloads}</td>
                <td>
                  <button onClick={() => handleView(file.id)}>
                    View
                  </button>{" "}

                  <button onClick={() => handleDownload(file.id)}>
                    Download
                  </button>{" "}

                  <button onClick={() => handleDelete(file.id)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

    </div>
  );
}
