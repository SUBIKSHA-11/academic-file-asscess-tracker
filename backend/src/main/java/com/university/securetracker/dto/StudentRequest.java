package com.university.securetracker.dto;

public class StudentRequest {

    private String name;
    private String email;
    private String phone;
    private String department;
    private Integer year;
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
