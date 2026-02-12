package com.university.securetracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.securetracker.model.Semester;
import com.university.securetracker.model.Subject;
import com.university.securetracker.repository.SemesterRepository;
import com.university.securetracker.repository.SubjectRepository;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    public Subject addSubject(Long semesterId, String name, String code) {

        Semester sem = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        Subject subject = new Subject();
        subject.setName(name);
        subject.setSubjectCode(code);
        subject.setSemester(sem);

        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
