package com.example.sms.controller;

import com.example.sms.dto.response.MaterialResponse;
import com.example.sms.service.MaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MaterialResponse> uploadMaterial(
            @RequestParam("classSubjectId") Long classSubjectId,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(materialService.uploadMaterial(classSubjectId, title, description, file));
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'HOD')")
    public ResponseEntity<List<MaterialResponse>> getMaterialsByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(materialService.getMaterialsForClass(classId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<MaterialResponse>> getMyMaterials() {
        return ResponseEntity.ok(materialService.getMyMaterials());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) throws IOException {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok("Material deleted successfully");
    }
}
