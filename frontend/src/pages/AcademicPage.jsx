import { useNavigate } from "react-router-dom";

export default function AcademicPage() {

  const navigate = useNavigate();

  const categories = [
    { name: "NOTES", label: "📘 Notes" },
    { name: "QUESTION_PAPER", label: "📝 Question Papers" },
    { name: "MARKSHEET", label: "📊 Marksheet" },
    { name: "ASSIGNMENT", label: "📂 Assignment" },
    { name: "LAB", label: "🧪 Lab" },
    { name: "OTHER", label: "📁 Other" }
  ];

  return (
    <div style={{ padding: "30px" }}>

      <h2>Academic Resources</h2>

      <div style={{
        display: "grid",
        gridTemplateColumns: "repeat(auto-fill, minmax(180px, 1fr))",
        gap: "20px"
      }}>
        {categories.map(cat => (
          <div
            key={cat.name}
            onClick={() => navigate(`/admin/files?category=${cat.name}`)}
            style={{
              padding: "25px",
              borderRadius: "10px",
              background: "#f4f4f4",
              cursor: "pointer",
              textAlign: "center",
              fontSize: "18px",
              fontWeight: "bold",
              boxShadow: "0 4px 10px rgba(0,0,0,0.05)"
            }}
          >
            {cat.label}
          </div>
        ))}
      </div>

    </div>
  );
}
