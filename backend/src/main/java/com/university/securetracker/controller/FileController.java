package com.university.securetracker.controller;

import com.university.securetracker.model.*;
import com.university.securetracker.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam Long unitId,
            @RequestParam SensitivityLevel level,
            @RequestParam MultipartFile file,
            Authentication authentication) throws IOException {

        String email = authentication.getName();

        return fileService.uploadFile(unitId, email, level, file);
    }

    @GetMapping("/unit/{unitId}")
    public List<AcademicFile> getFiles(@PathVariable Long unitId) {
        return fileService.getFilesByUnit(unitId);
    }
}
