package com.university.securetracker.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.AccessLog;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByUserEmailAndFileIdAndAccessTimeAfter(
        String userEmail,
        Long fileId,
        LocalDateTime time);

}
