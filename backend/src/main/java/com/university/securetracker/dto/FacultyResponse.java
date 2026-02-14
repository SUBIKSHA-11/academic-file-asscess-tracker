package com.university.securetracker.dto;

public class FacultyResponse {
    @SuppressWarnings("FieldMayBeFinal")
    private Long id;
     @SuppressWarnings("FieldMayBeFinal")
    private String facultyCode;
     @SuppressWarnings("FieldMayBeFinal")
    private String name;
     @SuppressWarnings("FieldMayBeFinal")
    private String email;
    @SuppressWarnings("FieldMayBeFinal")
    private String phone;
     @SuppressWarnings("FieldMayBeFinal")
    private String department;
    // ✅ Required constructor
    public FacultyResponse(Long id,
                           String facultyCode,
                           String name,
                           String email,
                           String phone,
                           String department) {
        this.id = id;
        this.facultyCode = facultyCode;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
    }

    // getters only
    public Long getId() { return id; }
    public String getFacultyCode() { return facultyCode; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDepartment() { return department; }
}
