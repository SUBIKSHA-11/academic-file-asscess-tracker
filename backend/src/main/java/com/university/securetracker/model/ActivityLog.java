package com.university.securetracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // UPLOAD, DOWNLOAD, VIEW, DELETE

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private AcademicFile file;

    private String ipAddress;

    private LocalDateTime timestamp = LocalDateTime.now();


    // Getters & Setters

    public Long getId() { return id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public AcademicFile getFile() { return file; }
    public void setFile(AcademicFile file) { this.file = file; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
