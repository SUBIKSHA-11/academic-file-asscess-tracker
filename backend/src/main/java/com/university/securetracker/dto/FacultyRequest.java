package com.university.securetracker.dto;


public class FacultyRequest {

    private String email;
    private String password;
    private String facultyCode;
    private String name;
    private String phone;
    private Long departmentId;

    // ===== getters setters =====

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFacultyCode() { return facultyCode; }
    public void setFacultyCode(String facultyCode) { this.facultyCode = facultyCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

   
}
