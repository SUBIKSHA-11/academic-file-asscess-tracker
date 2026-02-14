import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function AdminFacultyPage() {

  const [departments, setDepartments] = useState([]);
  const [selectedDept, setSelectedDept] = useState(null);
  const [facultyList, setFacultyList] = useState([]);
const [search, setSearch] = useState("");
const [page, setPage] = useState(0);
const [totalPages, setTotalPages] = useState(0);

  const [formData, setFormData] = useState({
    email: "",
    password: "",
    facultyCode: "",
    name: "",
    phone: "",
    departmentId: ""
  });

  // ===============================
  // FETCH DEPARTMENTS FIRST
  // ===============================
  useEffect(() => {
    fetchDepartments();
  }, []);

  const fetchDepartments = () => {
    api.get("/admin/departments")
      .then(res => setDepartments(res.data));
  };

  // ===============================
  // FETCH FACULTY BY DEPARTMENT
  // ===============================
 const fetchFaculty = (deptId, currentPage = 0) => {

  api.get(`/admin/faculty/department/${deptId}/paged`, {
    params: {
      search: search,
      page: currentPage,
      size: 5
    }
  })
  .then(res => {
    setFacultyList(res.data.content);
    setTotalPages(res.data.totalPages);
    setPage(currentPage);
  });
};


  // ===============================
  // HANDLE DEPARTMENT CLICK
  // ===============================
  const handleDepartmentClick = (dept) => {
    setSelectedDept(dept);
    setFormData({ ...formData, departmentId: dept.id });
    fetchFaculty(dept.id);
  };

  // ===============================
  // HANDLE FORM CHANGE
  // ===============================
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // ===============================
  // ADD FACULTY
  // ===============================
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await api.post("/admin/faculty", formData);
      alert("Faculty added successfully");
      fetchFaculty(selectedDept.id);
    } catch (err) {
      alert(err.response?.data?.message || "Something went wrong");
    }
  };

  // ===============================
  // DELETE FACULTY
  // ===============================
  const handleDelete = async (id) => {
    await api.delete(`/admin/faculty/${id}`);
    fetchFaculty(selectedDept.id);
  };

  return (
    <div style={{ padding: "20px" }}>

      <h2>Faculty Management</h2>

      {/* ===================== */}
      {/* DEPARTMENT CARDS */}
      {/* ===================== */}
      <h3>Select Department</h3>

      <div style={{ display: "flex", gap: "15px", flexWrap: "wrap" }}>
        {departments.map(dept => (
          <div
            key={dept.id}
            onClick={() => handleDepartmentClick(dept)}
            style={{
              padding: "15px",
              border: "1px solid #ccc",
              cursor: "pointer",
              background: selectedDept?.id === dept.id ? "#e0f0ff" : "white"
            }}
          >
            {dept.name}
          </div>
        ))}
      </div>

      {/* ===================== */}
      {/* FACULTY SECTION */}
      {/* ===================== */}
      {selectedDept && (
        <>
          <h3 style={{ marginTop: "30px" }}>
            Faculty - {selectedDept.name}
          </h3>

          {/* Add Faculty Form */}
          <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>

            <input name="email" placeholder="Email" onChange={handleChange} />
            <input name="password" placeholder="Password" onChange={handleChange} />
            <input name="facultyCode" placeholder="Faculty Code" onChange={handleChange} />
            <input name="name" placeholder="Name" onChange={handleChange} />
            <input name="phone" placeholder="Phone" onChange={handleChange} />

            <button type="submit">Add Faculty</button>
          </form>
          <input
  placeholder="Search by name..."
  value={search}
  onChange={(e) => setSearch(e.target.value)}
  onKeyUp={() => fetchFaculty(selectedDept.id, 0)}
/>


          {/* Faculty Table */}
          <table border="1" width="100%">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Faculty Code</th>
                <th>Phone</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {facultyList.map(f => (
                <tr key={f.id}>
                  <td>{f.name}</td>
                  <td>{f.email}</td>
                  <td>{f.facultyCode}</td>
                  <td>{f.phone}</td>
                  <td>
                    <button onClick={() => handleDelete(f.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div style={{ marginTop: "15px" }}>
  {Array.from({ length: totalPages }, (_, i) => (
    <button
      key={i}
      onClick={() => fetchFaculty(selectedDept.id, i)}
      style={{
        marginRight: "5px",
        background: page === i ? "#007bff" : "#eee",
        color: page === i ? "white" : "black"
      }}
    >
      {i + 1}
    </button>
  ))}
</div>

        </>
      )}

    </div>
  );
}
