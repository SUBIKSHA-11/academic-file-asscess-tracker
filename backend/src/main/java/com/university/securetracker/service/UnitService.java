package com.university.securetracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.securetracker.model.Subject;
import com.university.securetracker.model.Unit;
import com.university.securetracker.repository.SubjectRepository;
import com.university.securetracker.repository.UnitRepository;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public Unit addUnit(Long subjectId, String unitName) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Unit unit = new Unit();
        unit.setUnitName(unitName);
        unit.setSubject(subject);

        return unitRepository.save(unit);
    }

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public void deleteUnit(Long id) {
        unitRepository.deleteById(id);
    }
}
