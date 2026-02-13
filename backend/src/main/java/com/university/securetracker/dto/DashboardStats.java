package com.university.securetracker.dto;

public class DashboardStats {

    private long totalUsers;
    private long totalFaculty;
    private long totalStudents;

    // 🔥 manual constructor (IMPORTANT)
    public DashboardStats(long totalUsers, long totalFaculty, long totalStudents) {
        this.totalUsers = totalUsers;
        this.totalFaculty = totalFaculty;
        this.totalStudents = totalStudents;
    }

    // empty constructor
    public DashboardStats() {
    }

    // getters setters
    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalFaculty() {
        return totalFaculty;
    }

    public void setTotalFaculty(long totalFaculty) {
        this.totalFaculty = totalFaculty;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }
}
