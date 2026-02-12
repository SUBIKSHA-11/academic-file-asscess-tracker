package com.university.securetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {
}
