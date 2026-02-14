package com.university.securetracker.controller;

import com.university.securetracker.model.AcademicFile;
import com.university.securetracker.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin/analytics")
public class AdminAnalyticsController {

    private final FileRepository fileRepo;
    private final ActivityLogRepository logRepo;

    public AdminAnalyticsController(FileRepository fileRepo,
                                    ActivityLogRepository logRepo) {
        this.fileRepo = fileRepo;
        this.logRepo = logRepo;
    }

    // 🔥 Top Downloaded Files
    @GetMapping("/top-files")
    public List<AcademicFile> topFiles() {
        return fileRepo.findTopDownloadedFiles()
                .stream()
                .limit(5)
                .toList();
    }

    // 🔥 Most Active Users
    @GetMapping("/top-users")
    public List<Map<String, Object>> topUsers() {

        List<Object[]> results = logRepo.findMostActiveUsers();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> data = new HashMap<>();
            data.put("userId", row[0]);
            data.put("activityCount", row[1]);
            response.add(data);
        }

        return response;
    }

    // 🔥 Category Distribution
    @GetMapping("/category-stats")
    public Map<String, Long> categoryStats() {

        Map<String, Long> stats = new HashMap<>();

        for (AcademicFile file : fileRepo.findAll()) {
            String category = file.getCategory().name();
            stats.put(category,
                    stats.getOrDefault(category, 0L) + 1);
        }

        return stats;
    }

    // 🔥 Department Distribution
    @GetMapping("/department-stats")
    public Map<String, Long> departmentStats() {

        Map<String, Long> stats = new HashMap<>();

        for (AcademicFile file : fileRepo.findAll()) {
            String dept = file.getDepartment();
            stats.put(dept,
                    stats.getOrDefault(dept, 0L) + 1);
        }

        return stats;
    }
}
