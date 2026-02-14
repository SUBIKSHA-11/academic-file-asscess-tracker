package com.university.securetracker.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "faculty_details")
public class FacultyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // USER LINK
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private String facultyCode;
    private String name;
    private String email;
    private String phone;

    // ✅ PROPER RELATION
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFacultyCode() { return facultyCode; }
    public void setFacultyCode(String facultyCode) { this.facultyCode = facultyCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}
