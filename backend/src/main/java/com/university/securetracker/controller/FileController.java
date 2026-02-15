package com.university.securetracker.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.university.securetracker.dto.FileUploadRequest;
import com.university.securetracker.model.AcademicFile;
import com.university.securetracker.service.FileService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    // ================= UPLOAD =================
    @PostMapping("/upload")
    @SuppressWarnings("UnnecessaryTemporaryOnConversionFromString")
    public AcademicFile upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("departmentId") Long departmentId,
            @RequestParam("semester") String semester,
            @RequestParam("subject") String subject,
            @RequestParam("category") String category,
            @RequestParam("sensitivity") String sensitivity,
            Authentication auth,
            HttpServletRequest request) throws IOException {

        FileUploadRequest req = new FileUploadRequest();
        req.setDepartment(departmentId);

        try {
            req.setSemester(Integer.parseInt(semester));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid semester value");
        }

        req.setSubject(subject);
        req.setCategory(category.toUpperCase());
        req.setSensitivity(sensitivity.toUpperCase());

        return service.upload(file, req, auth.getName(), request.getRemoteAddr());
    }


    // ================= LIST =================
    @GetMapping
    public List<AcademicFile> all(Authentication auth) {
        return service.all(auth.getName());
    }


    // ================= FILTERS =================
    @GetMapping("/department/{dept}")
    public List<AcademicFile> byDept(@PathVariable Long dept,
                                     Authentication auth) {
        return service.byDept(dept, auth.getName());
    }

    @GetMapping("/department/{dept}/semester/{sem}")
    public List<AcademicFile> byDeptSem(@PathVariable Long dept,
                                        @PathVariable Integer sem,
                                        Authentication auth) {
        return service.byDeptSem(dept, sem, auth.getName());
    }

    @GetMapping("/subject/{subject}")
    public List<AcademicFile> bySubject(@PathVariable String subject,
                                        Authentication auth) {
        return service.bySubject(subject, auth.getName());
    }


    // ================= DOWNLOAD =================
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id,
                                           Authentication auth,
                                           HttpServletRequest request) throws IOException {

        byte[] data = service.download(id, auth.getName(), request.getRemoteAddr());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file")
                .body(data);
    }


    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,
                         Authentication auth,
                         HttpServletRequest request) {

        service.delete(id, auth.getName(), request.getRemoteAddr());
        return "Deleted";
    }
    @GetMapping("/{id}/view")
public ResponseEntity<byte[]> view(@PathVariable Long id,
                                   Authentication auth,
                                   jakarta.servlet.http.HttpServletRequest request)
        throws IOException {

    byte[] data = service.view(id,
                               auth.getName(),
                               request.getRemoteAddr());

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=file")
            .body(data);
}
@GetMapping("/category/{category}")
public List<AcademicFile> byCategory(@PathVariable String category,
                                     Authentication auth) {
    return service.byCategory(category, auth.getName());
}

}
