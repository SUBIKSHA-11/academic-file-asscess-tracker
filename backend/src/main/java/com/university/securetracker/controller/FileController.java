package com.university.securetracker.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
//import com.university.securetracker.repository.TemporaryAccessRepository;

import com.university.securetracker.model.AcademicFile;
import com.university.securetracker.model.DownloadToken;
import com.university.securetracker.model.SensitivityLevel;
import com.university.securetracker.model.TemporaryAccess;
import com.university.securetracker.repository.DownloadTokenRepository;
import com.university.securetracker.repository.TemporaryAccessRepository;
//import com.university.securetracker.model.SensitivityLevel;
import com.university.securetracker.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;
@Autowired
private TemporaryAccessRepository temporaryAccessRepository;
@Autowired
private DownloadTokenRepository downloadTokenRepository;

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
   //@PreAuthorize("permitAll()")
  @PostMapping("/upload")
public String uploadFile(
        @RequestParam Long unitId,
        @RequestParam String level,
        @RequestParam MultipartFile file,
        Authentication authentication) throws IOException {

    String email = authentication.getName();

    SensitivityLevel sensitivity =
            SensitivityLevel.valueOf(level.toUpperCase());

    return fileService.uploadFile(
            unitId,
            email,
            sensitivity,
            file
    );
    
}


    @GetMapping("/unit/{unitId}")
    public List<AcademicFile> getFiles(@PathVariable Long unitId) {
        return fileService.getFilesByUnit(unitId);
    }
    @GetMapping("/debug")
public String debug(Authentication authentication) {
    return authentication.getAuthorities().toString();
}
@PreAuthorize("isAuthenticated()")
@GetMapping("/download/{fileId}")
public ResponseEntity<Resource> downloadFile(
        @PathVariable Long fileId,
        Authentication authentication) throws IOException {

    return fileService.downloadFile(fileId, authentication);
}
@PostMapping("/grant-temporary")
@PreAuthorize("hasRole('ADMIN')")
public String grantTemporaryAccess(
        @RequestParam String userEmail,
        @RequestParam Long fileId,
        @RequestParam int hours) {

    TemporaryAccess access = new TemporaryAccess();
    access.setUserEmail(userEmail);
    access.setFileId(fileId);
    access.setExpiryTime(
            LocalDateTime.now().plusHours(hours));

    temporaryAccessRepository.save(access);

    return "Temporary access granted for " + hours + " hours";
}
@PostMapping("/generate-secure-link")
@PreAuthorize("hasRole('ADMIN')")
public String generateSecureLink(
        @RequestParam Long fileId,
        @RequestParam int minutes) {

    String token = java.util.UUID.randomUUID().toString();

    DownloadToken downloadToken = new DownloadToken();
    downloadToken.setToken(token);
    downloadToken.setFileId(fileId);
    downloadToken.setExpiryTime(
            LocalDateTime.now().plusMinutes(minutes));

    downloadTokenRepository.save(downloadToken);

    return "Secure link: http://localhost:8080/files/secure-download?token=" + token;
}
@GetMapping("/secure-download")
public ResponseEntity<Resource> secureDownload(
        @RequestParam String token,
        Authentication authentication) throws IOException {

    DownloadToken downloadToken =
            downloadTokenRepository.findByToken(token)
                    .orElseThrow(() ->
                            new RuntimeException("Invalid token"));

    if (downloadToken.getExpiryTime()
            .isBefore(LocalDateTime.now())) {

        throw new RuntimeException("Link expired");
    }

    // 🔥 Delete token immediately (Self-destruct)
    downloadTokenRepository.deleteByToken(token);

    return fileService.downloadFile(
            downloadToken.getFileId(),
            authentication);
}

}
