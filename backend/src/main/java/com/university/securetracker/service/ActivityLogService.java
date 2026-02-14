package com.university.securetracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.university.securetracker.model.AcademicFile;
import com.university.securetracker.model.ActivityLog;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.ActivityLogRepository;

@Service
public class ActivityLogService {

    private final ActivityLogRepository logRepo;

    public ActivityLogService(ActivityLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    public void log(String action, User user, AcademicFile file, String ip) {

        ActivityLog log = new ActivityLog();
        log.setAction(action);
        log.setUser(user);
        log.setFile(file);
        log.setIpAddress(ip);

        logRepo.save(log);
    }

    public List<ActivityLog> getAll() {
        return logRepo.findAll();
    }

    public List<ActivityLog> byUser(Long userId) {
        return logRepo.findByUserId(userId);
    }
}
