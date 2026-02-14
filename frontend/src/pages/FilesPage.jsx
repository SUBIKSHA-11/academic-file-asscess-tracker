import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function FilesPage() {

  const [files, setFiles] = useState([]);
  const [fileData, setFileData] = useState({
    department: "",
    semester: "",
    subject: "",
    category: "NOTES",
    sensitivity: "PUBLIC"
  });

  const [selectedFile, setSelectedFile] = useState(null);

  // Fetch all files
  const fetchFiles = () => {
    api.get("/files")
      .then(res => setFiles(res.data));
  };

  useEffect(() => {
    fetchFiles();
  }, []);

  // Upload file
  const handleUpload = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("file", selectedFile);
    formData.append("department", fileData.department);
    formData.append("semester", fileData.semester);
    formData.append("subject", fileData.subject);
    formData.append("category", fileData.category);
    formData.append("sensitivity", fileData.sensitivity);

    try {
      await api.post("/files/upload", formData);
      alert("File uploaded");
      fetchFiles();
    } catch (err) {
      alert(err.response?.data || "Upload failed");
    }
  };

  // Delete file
  const handleDelete = async (id) => {
    await api.delete(`/files/${id}`);
    fetchFiles();
  };

  // Download
  const handleDownload = (id) => {
    window.open(`http://localhost:8080/files/${id}/download`);
  };

  // View
  const handleView = (id) => {
    window.open(`http://localhost:8080/files/${id}/view`);
  };

  return (
    <div>

      <h2>Files Module</h2>

      {/* Upload Form */}
      <form onSubmit={handleUpload} style={{ marginBottom: "20px" }}>

        <input placeholder="Department"
          onChange={e =>
            setFileData({ ...fileData, department: e.target.value })
          } />

        <input placeholder="Semester"
          onChange={e =>
            setFileData({ ...fileData, semester: e.target.value })
          } />

        <input placeholder="Subject"
          onChange={e =>
            setFileData({ ...fileData, subject: e.target.value })
          } />

        <select onChange={e =>
          setFileData({ ...fileData, category: e.target.value })
        }>
          <option value="NOTES">NOTES</option>
          <option value="QUESTION_PAPER">QUESTION_PAPER</option>
          <option value="MARKSHEET">MARKSHEET</option>
          <option value="ASSIGNMENT">ASSIGNMENT</option>
          <option value="LAB">LAB</option>
        </select>

        <select onChange={e =>
          setFileData({ ...fileData, sensitivity: e.target.value })
        }>
          <option value="PUBLIC">PUBLIC</option>
          <option value="INTERNAL">INTERNAL</option>
          <option value="CONFIDENTIAL">CONFIDENTIAL</option>
        </select>

        <input type="file"
          onChange={e => setSelectedFile(e.target.files[0])} />

        <button type="submit">Upload</button>
      </form>

      {/* Files Table */}
      <table border="1" width="100%">
        <thead>
          <tr>
            <th>Name</th>
            <th>Dept</th>
            <th>Sem</th>
            <th>Category</th>
            <th>Sensitivity</th>
            <th>Downloads</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {files.map(file => (
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
                </button>

                <button onClick={() => handleDownload(file.id)}>
                  Download
                </button>

                <button onClick={() => handleDelete(file.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

    </div>
  );
}
