package com.university.securetracker.dto;


public class FileUploadRequest {

    private Long departmentId;
    private Integer semester;
    private String subject;
    private String category;
    private String sensitivity;

    public FileUploadRequest() {
    }

    // getters setters

    public Long getDepartment() { return departmentId; }
    public void setDepartment(Long departmentId) { this.departmentId = departmentId; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSensitivity() { return sensitivity; }
    public void setSensitivity(String sensitivity) { this.sensitivity = sensitivity; }
}

