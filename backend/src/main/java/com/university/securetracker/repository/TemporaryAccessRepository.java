package com.university.securetracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.TemporaryAccess;

public interface TemporaryAccessRepository
        extends JpaRepository<TemporaryAccess, Long> {

    Optional<TemporaryAccess>
    findByUserEmailAndFileId(String userEmail, Long fileId);
}
