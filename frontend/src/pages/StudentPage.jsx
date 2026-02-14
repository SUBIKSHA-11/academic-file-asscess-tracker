import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function AdminStudentPage() {

  const [departments, setDepartments] = useState([]);
  const [selectedDept, setSelectedDept] = useState(null);
  const [students, setStudents] = useState([]);
  const [yearCount, setYearCount] = useState({});
  const [editId, setEditId] = useState(null);
const [search, setSearch] = useState("");
const [page, setPage] = useState(0);
const [totalPages, setTotalPages] = useState(0);

  const [formData, setFormData] = useState({
    email: "",
    password: "",
    regNo: "",
    name: "",
    phone: "",
    year: "",
    departmentId: ""
  });

  // =====================
  // FETCH DEPARTMENTS
  // =====================
  useEffect(() => {
    api.get("/admin/departments")
      .then(res => setDepartments(res.data));
  }, []);

  // =====================
  // FETCH STUDENTS
  // =====================
  const fetchStudents = (deptId, currentPage = 0) => {

  api.get(`/admin/students/department/${deptId}/paged`, {
    params: {
      search: search,
      page: currentPage,
      size: 5
    }
  })
  .then(res => {
    setStudents(res.data.content);
    setTotalPages(res.data.totalPages);
    setPage(currentPage);
  });
};

  const handleDeptClick = (dept) => {
    setSelectedDept(dept);
    setFormData({ ...formData, departmentId: dept.id });
    fetchStudents(dept.id);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // =====================
  // ADD OR UPDATE
  // =====================
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {

      if (editId) {
        await api.put(`/admin/students/${editId}`, formData);
        setEditId(null);
      } else {
        await api.post("/admin/students", formData);
      }

      fetchStudents(selectedDept.id);

    } catch (err) {
      alert(err.response?.data?.message || "Something went wrong");
    }
  };

  // =====================
  // DELETE
  // =====================
  const handleDelete = async (id) => {
    await api.delete(`/admin/students/${id}`);
    fetchStudents(selectedDept.id);
  };

  // =====================
  // EDIT
  // =====================
  const handleEdit = (student) => {
    setEditId(student.id);
    setFormData({
      ...student,
      departmentId: selectedDept.id
    });
  };

  return (
    <div style={{ padding: "20px" }}>

      <h2>Student Management</h2>

      {/* Department Cards */}
      <div style={{ display: "flex", gap: "15px", marginBottom: "20px" }}>
        {departments.map(dept => (
          <div key={dept.id}
            onClick={() => handleDeptClick(dept)}
            style={{
              padding: "15px",
              border: "1px solid #ccc",
              cursor: "pointer",
              background: selectedDept?.id === dept.id ? "#dff0ff" : "white"
            }}>
            {dept.name}
          </div>
        ))}
      </div>

      {/* Student Section */}
      {selectedDept && (
        <>
          <h3>{selectedDept.name} - Students</h3>

          {/* Year Count */}
          <div style={{ marginBottom: "15px" }}>
            {Object.entries(yearCount).map(([year, count]) => (
              <span key={year}
                style={{
                  marginRight: "15px",
                  padding: "5px 10px",
                  background: "#eee",
                  borderRadius: "5px"
                }}>
                Year {year}: {count}
              </span>
            ))}
          </div>

          {/* Form */}
          <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
            <input name="email" placeholder="Email" onChange={handleChange} value={formData.email} />
            <input name="password" placeholder="Password" onChange={handleChange} />
            <input name="regNo" placeholder="Reg No" onChange={handleChange} value={formData.regNo} />
            <input name="name" placeholder="Name" onChange={handleChange} value={formData.name} />
            <input name="phone" placeholder="Phone" onChange={handleChange} value={formData.phone} />
            <input name="year" placeholder="Year" onChange={handleChange} value={formData.year} />
            <button type="submit">{editId ? "Update" : "Add"} Student</button>
          </form>
<input
  placeholder="Search student..."
  value={search}
  onChange={(e) => setSearch(e.target.value)}
  onKeyUp={() => fetchStudents(selectedDept.id, 0)}
/>

          {/* Table */}
          <table border="1" width="100%">
            <thead>
              <tr>
                <th>Name</th>
                <th>Reg No</th>
                <th>Year</th>
                <th>Phone</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {students.map(s => (
                <tr key={s.id}>
                  <td>{s.name}</td>
                  <td>{s.regNo}</td>
                  <td>{s.year}</td>
                  <td>{s.phone}</td>
                  <td>
                    <button onClick={() => handleEdit(s)}>Edit</button>
                    <button onClick={() => handleDelete(s.id)}>Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
<div style={{ marginTop: "15px" }}>
  {Array.from({ length: totalPages }, (_, i) => (
    <button
      key={i}
      onClick={() => fetchStudents(selectedDept.id, i)}
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
