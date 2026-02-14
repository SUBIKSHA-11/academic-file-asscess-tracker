package com.university.securetracker.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.university.securetracker.dto.FileUploadRequest;
import com.university.securetracker.model.*;
import com.university.securetracker.repository.*;

@Service
public class FileService {

    private final FileRepository repo;
    private final UserRepository userRepo;
    private final ActivityLogRepository logRepo;
    private final AlertRepository alertRepo;

    private final String UPLOAD_DIR = "uploads/files/";
private final List<String> allowedExtensions = List.of(
        "pdf", "docx", "pptx", "xlsx",
        "jpg", "png", "mp4", "zip"
);
private void validateFile(MultipartFile file) {

    String originalName = file.getOriginalFilename();

    if (originalName == null || !originalName.contains(".")) {
        throw new RuntimeException("Invalid file name");
    }

    String extension = originalName.substring(
            originalName.lastIndexOf(".") + 1
    ).toLowerCase();

    if (!allowedExtensions.contains(extension)) {
        throw new RuntimeException("File type not allowed");
    }

    // Size validation (20MB limit)
    long maxSize = 20 * 1024 * 1024; // 20MB

    if (file.getSize() > maxSize) {
        throw new RuntimeException("File size exceeds 20MB limit");
    }
}

    public FileService(FileRepository repo,
                       UserRepository userRepo,
                       ActivityLogRepository logRepo,
                    AlertRepository alertRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.logRepo = logRepo;
        this.alertRepo = alertRepo;
    }

    // =========================================================
    // 🔐 ACCESS CONTROL
    // =========================================================

    private void checkAccess(User user, AcademicFile file) {

        Sensitivity sensitivity = file.getSensitivity();
        String role = user.getRole();

        if (role.equals("ADMIN")) return;

        if (role.equals("FACULTY")) {
            if (sensitivity == Sensitivity.PUBLIC ||
                sensitivity == Sensitivity.INTERNAL) return;
        }

        if (role.equals("STUDENT")) {
            if (sensitivity == Sensitivity.PUBLIC) return;
        }

        throw new RuntimeException("Access Denied");
    }

    // =========================================================
    // 📜 LOGGING
    // =========================================================

    private void logAction(User user,
                           AcademicFile file,
                           String action,
                           String ip) {

        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setFile(file);
        log.setAction(action);
        log.setIpAddress(ip);

        logRepo.save(log);
    }

    // =========================================================
    // 📤 UPLOAD
    // =========================================================

    public AcademicFile upload(MultipartFile file,
                               FileUploadRequest req,
                               String email,
                               String ip) throws IOException {
validateFile(file);

        new File(UPLOAD_DIR).mkdirs();

        String uniqueName =
                UUID.randomUUID() + "_" + file.getOriginalFilename();

        String path = UPLOAD_DIR + uniqueName;

        file.transferTo(new File(path));

        User user = userRepo.findByEmail(email).orElseThrow();
if (!user.getRole().equals("ADMIN") &&
    !user.getRole().equals("FACULTY")) {

    throw new RuntimeException("Only Faculty or Admin can upload files");
}

        AcademicFile af = new AcademicFile();
        af.setFileName(file.getOriginalFilename());
        af.setFilePath(path);
        af.setDepartment(req.getDepartment());
        af.setSemester(req.getSemester());
        af.setSubject(req.getSubject());
        af.setSize(file.getSize());
        af.setOwner(user);

        af.setCategory(FileCategory.valueOf(req.getCategory()));
        af.setSensitivity(Sensitivity.valueOf(req.getSensitivity()));

        repo.save(af);

        logAction(user, af, "UPLOAD", ip);

        return af;
    }

    // =========================================================
    // 📋 LIST (with sensitivity filter)
    // =========================================================

    public List<AcademicFile> all(String email) {

        User user = userRepo.findByEmail(email).orElseThrow();

        return repo.findAll()
                .stream()
                .filter(f -> {
                    try {
                        checkAccess(user, f);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<AcademicFile> byDept(String dept, String email) {

        User user = userRepo.findByEmail(email).orElseThrow();

        return repo.findByDepartment(dept)
                .stream()
                .filter(f -> {
                    try {
                        checkAccess(user, f);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<AcademicFile> byDeptSem(String dept,
                                        Integer sem,
                                        String email) {

        User user = userRepo.findByEmail(email).orElseThrow();

        return repo.findByDepartmentAndSemester(dept, sem)
                .stream()
                .filter(f -> {
                    try {
                        checkAccess(user, f);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<AcademicFile> bySubject(String subject,
                                        String email) {

        User user = userRepo.findByEmail(email).orElseThrow();

        return repo.findBySubject(subject)
                .stream()
                .filter(f -> {
                    try {
                        checkAccess(user, f);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // =========================================================
    // 📥 DOWNLOAD
    // =========================================================

    public byte[] download(Long id,
                           String email,
                           String ip) throws IOException {

        AcademicFile f = repo.findById(id).orElseThrow();
        User user = userRepo.findByEmail(email).orElseThrow();

        checkAccess(user, f);

        f.setDownloads(f.getDownloads() + 1);
        repo.save(f);

        logAction(user, f, "DOWNLOAD", ip);

        checkSuspiciousDownload(user);
        return Files.readAllBytes(new File(f.getFilePath()).toPath());
    }

    // =========================================================
    // ❌ DELETE
    // =========================================================

    public void delete(Long id,
                       String email,
                       String ip) {

        AcademicFile f = repo.findById(id).orElseThrow();
        User user = userRepo.findByEmail(email).orElseThrow();

        if (!user.getRole().equals("ADMIN") &&
            !f.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Only owner or admin can delete");
        }

        new File(f.getFilePath()).delete();
        repo.delete(f);

        logAction(user, f, "DELETE", ip);
    }
    // =========================================================
// 👁 VIEW
// =========================================================

public byte[] view(Long id,
                   String email,
                   String ip) throws IOException {

    AcademicFile f = repo.findById(id).orElseThrow();
    User user = userRepo.findByEmail(email).orElseThrow();

    checkAccess(user, f);

    logAction(user, f, "VIEW", ip);

    return Files.readAllBytes(new File(f.getFilePath()).toPath());
}
private void checkSuspiciousDownload(User user) {

    LocalDateTime twoMinutesAgo =
            LocalDateTime.now().minusMinutes(2);

    long recentDownloads =
            logRepo.countByUserIdAndActionAndTimestampAfter(
                    user.getId(),
                    "DOWNLOAD",
                    twoMinutesAgo
            );

    if (recentDownloads >= 5) {

        Alert alert = new Alert();
        alert.setUser(user);
        alert.setReason("5+ downloads within 2 minutes");
        alert.setSeverity("HIGH");

        alertRepo.save(alert);
    }
}


}
