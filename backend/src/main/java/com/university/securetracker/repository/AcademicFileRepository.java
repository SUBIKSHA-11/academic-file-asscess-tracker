package com.university.securetracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.AcademicFile;

public interface AcademicFileRepository extends JpaRepository<AcademicFile, Long> {

    List<AcademicFile> findByUnitId(Long unitId);
}
