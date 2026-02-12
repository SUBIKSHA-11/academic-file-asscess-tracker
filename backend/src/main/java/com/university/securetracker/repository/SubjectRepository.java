package com.university.securetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
