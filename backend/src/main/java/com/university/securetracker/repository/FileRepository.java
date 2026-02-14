package com.university.securetracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.university.securetracker.model.AcademicFile;

public interface FileRepository extends JpaRepository<AcademicFile, Long> {

    List<AcademicFile> findByDepartment(String department);

    List<AcademicFile> findByDepartmentAndSemester(String department, Integer semester);

    List<AcademicFile> findBySubject(String subject);
    @Query("SELECT f FROM AcademicFile f ORDER BY f.downloads DESC")
    List<AcademicFile> findTopDownloadedFiles();
}
