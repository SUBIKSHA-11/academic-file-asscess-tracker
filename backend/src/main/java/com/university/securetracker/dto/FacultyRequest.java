package com.university.securetracker.dto;

public class FacultyRequest {

    private String name;
    private String email;
    private String phone;
    private String department;
    private String facultyCode;
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getFacultyCode() { return facultyCode; }
    public void setFacultyCode(String facultyCode) { this.facultyCode = facultyCode; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
