package com.university.securetracker.dto;

public class StudentResponse {

    private final Long id;
    private final String registerNumber;
    private final String name;
    private final String email;
    private final String phone;
    private final Integer year;
    private final Long departmentId;
    private final String departmentName;

    // ✅ Constructor
    public StudentResponse(Long id,
                           String registerNumber,
                           String name,
                           String email,
                           String phone,
                           Integer year,
                           Long departmentId,
                           String departmentName) {

        this.id = id;
        this.registerNumber = registerNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.year = year;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    // ======================
    // GETTERS ONLY
    // ======================

    public Long getId() {
        return id;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getYear() {
        return year;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}
