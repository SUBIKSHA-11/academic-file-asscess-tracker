package com.university.securetracker.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.university.securetracker.model.AcademicFile;
import com.university.securetracker.model.SensitivityLevel;
import com.university.securetracker.model.Unit;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.AcademicFileRepository;
import com.university.securetracker.repository.UnitRepository;
import com.university.securetracker.repository.UserRepository;

@Service
public class FileService {

    @Autowired
    private AcademicFileRepository fileRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UserRepository userRepository;

    private final String STORAGE_PATH = "secure_storage/";

    public String uploadFile(Long unitId,
                             String uploaderEmail,
                             SensitivityLevel level,
                             MultipartFile file) throws IOException {

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        User user = userRepository.findByEmail(uploaderEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Version control
        List<AcademicFile> existingFiles =
                fileRepository.findByUnitId(unitId);

        int version = existingFiles.size() + 1;

        String fileName = file.getOriginalFilename();
        String storedName = version + "_" + fileName;

        File directory = new File(STORAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File destination = new File(STORAGE_PATH + storedName);
        file.transferTo(destination);

        AcademicFile academicFile = new AcademicFile();
        academicFile.setFileName(fileName);
        academicFile.setFilePath(destination.getAbsolutePath());
        academicFile.setSensitivity(level);
        academicFile.setVersion(version);
        academicFile.setUploadedAt(LocalDateTime.now());
        academicFile.setUnit(unit);
        academicFile.setUploadedBy(user);

        fileRepository.save(academicFile);

        return "File uploaded successfully with version " + version;
    }

    public List<AcademicFile> getFilesByUnit(Long unitId) {
        return fileRepository.findByUnitId(unitId);
    }
}
