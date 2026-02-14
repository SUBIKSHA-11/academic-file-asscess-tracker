package com.university.securetracker.controller;

import com.university.securetracker.model.ActivityLog;
import com.university.securetracker.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/logs")
@RequiredArgsConstructor
public class AdminLogsController {

    private final ActivityLogRepository repo;

    @GetMapping
    public List<ActivityLog> all() {
        return repo.findAll();
    }
}

