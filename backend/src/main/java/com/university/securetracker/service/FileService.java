package com.university.securetracker.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
//import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.university.securetracker.model.AcademicFile;
import com.university.securetracker.model.SensitivityLevel;
import com.university.securetracker.model.TemporaryAccess;
import com.university.securetracker.model.Unit;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.AcademicFileRepository;
import com.university.securetracker.repository.AccessLogRepository;
import com.university.securetracker.repository.TemporaryAccessRepository;
import com.university.securetracker.repository.UnitRepository;
import com.university.securetracker.repository.UserRepository;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

import com.university.securetracker.model.AccessLog;

@Service
public class FileService {

    @Autowired
    private AcademicFileRepository fileRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
private AccessLogRepository accessLogRepository;
@Autowired
private TemporaryAccessRepository temporaryAccessRepository;


    public String uploadFile(Long unitId,
                             String uploaderEmail,
                             SensitivityLevel level,
                             MultipartFile file) throws IOException {

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        User uploader = userRepository.findByEmail(uploaderEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String uploadDir = System.getProperty("user.dir") + "/secure_storage";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = unitId + "_" + file.getOriginalFilename();

        File destination = new File(directory, fileName);
        file.transferTo(destination);

        AcademicFile academicFile = new AcademicFile();
        academicFile.setFileName(fileName);
        academicFile.setFilePath(destination.getAbsolutePath());
        academicFile.setSensitivity(level);
        academicFile.setUnit(unit);
        academicFile.setUploadedBy(uploader);

        fileRepository.save(academicFile); // ✅ FIXED

        return "File uploaded successfully";
    }

    public List<AcademicFile> getFilesByUnit(Long unitId) {
        return fileRepository.findByUnitId(unitId);
    }
    public ResponseEntity<Resource> downloadFile(
        Long fileId,
        Authentication authentication) throws IOException {

    AcademicFile file = fileRepository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found"));

    String userRole = authentication.getAuthorities()
            .iterator().next().getAuthority();

  if (file.getSensitivity() == SensitivityLevel.HIGH) {

    if (!userRole.equals("ROLE_ADMIN")) {

        Optional<TemporaryAccess> tempAccess =
                temporaryAccessRepository
                        .findByUserEmailAndFileId(
                                authentication.getName(),
                                fileId);

        if (tempAccess.isEmpty() ||
            tempAccess.get().getExpiryTime()
                    .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Access Denied - Temporary access expired");
        }
    }
}


    if (file.getSensitivity() == SensitivityLevel.MEDIUM &&
            userRole.equals("ROLE_STUDENT")) {
        throw new RuntimeException("Access Denied - Medium Sensitivity");
    }

    // =====================================================
    // 🔥 ADD SUSPICIOUS LOGIC HERE
    // =====================================================

    boolean suspicious = false;
    LocalDateTime now = LocalDateTime.now();

    // Rule 1
    if (file.getSensitivity() == SensitivityLevel.HIGH &&
            userRole.equals("ROLE_STUDENT")) {
        suspicious = true;
    }

    // Rule 2
    LocalDateTime tenMinutesAgo = now.minusMinutes(10);

    List<AccessLog> recentAccess =
            accessLogRepository
            .findByUserEmailAndFileIdAndAccessTimeAfter(
                    authentication.getName(),
                    fileId,
                    tenMinutesAgo);

    if (recentAccess.size() >= 5) {
        suspicious = true;
    }

    // Rule 3
    int hour = now.getHour();
    if (hour >= 0 && hour <= 4) {
        suspicious = true;
    }

    // =====================================================
    // 🔥 SAVE LOG HERE
    // =====================================================

    AccessLog log = new AccessLog();
    log.setUserEmail(authentication.getName());
    log.setRole(userRole);
    log.setFileId(fileId);
    log.setAccessTime(now);
    log.setSuspicious(suspicious);

    accessLogRepository.save(log);

    // =====================================================

    File physicalFile = new File(file.getFilePath());
    Resource resource = new UrlResource(physicalFile.toURI());

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFileName() + "\"")
            .body(resource);
}


}
