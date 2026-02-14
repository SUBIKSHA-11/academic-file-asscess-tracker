package com.university.securetracker.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "student_details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StudentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private String regNo;
    private String name;
    private String email;
    private String phone;

    private Integer year;

    // ✅ RELATION WITH DEPARTMENT
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // ===== Getters & Setters =====

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getRegNo() { return regNo; }

    public void setRegNo(String regNo) { this.regNo = regNo; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public Integer getYear() { return year; }

    public void setYear(Integer year) { this.year = year; }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }
}
