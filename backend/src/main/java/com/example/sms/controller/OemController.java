package com.example.sms.controller;

import com.example.sms.dto.request.OemMarksUpdateRequest;
import com.example.sms.dto.request.OemSheetCreateRequest;
import com.example.sms.dto.response.OemSheetDto;
import com.example.sms.dto.response.StudentMarksResponse;
import com.example.sms.service.OemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/marks")
public class OemController {
    private final OemService oemService;

    public OemController(OemService oemService) {
        this.oemService = oemService;
    }

    @PostMapping("/sheet")
    @PreAuthorize("hasAnyRole('HOD', 'TEACHER')")
    public ResponseEntity<OemSheetDto> createSheet(@Valid @RequestBody OemSheetCreateRequest request) {
        return ResponseEntity.ok(oemService.createSheet(request));
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('HOD', 'CLASS_ADVISOR', 'TEACHER')")
    public ResponseEntity<List<OemSheetDto>> getSheetsForClass(@PathVariable Long classId) {
        return ResponseEntity.ok(oemService.getSheetsByClass(classId));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('HOD', 'CLASS_ADVISOR', 'STUDENT')")
    public ResponseEntity<StudentMarksResponse> getStudentMarks(@PathVariable Long studentId) {
        // Validation for student accessing their own marks could be added using userContextService
        return ResponseEntity.ok(oemService.getStudentMarks(studentId));
    }

    @PutMapping("/sheet/{sheetId}/marks")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<OemSheetDto> updateMarks(@PathVariable Long sheetId, @Valid @RequestBody OemMarksUpdateRequest request) {
        if (!request.getSheetId().equals(sheetId)) {
            throw new RuntimeException("Path variable sheetId does not match request body sheetId");
        }
        return ResponseEntity.ok(oemService.updateMarks(request));
    }

    @PutMapping("/sheet/{sheetId}/submit")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> submitSheet(@PathVariable Long sheetId) {
        oemService.submitSheet(sheetId);
        return ResponseEntity.ok("Marks sheet submitted successfully");
    }

    @PutMapping("/sheet/{sheetId}/lock")
    @PreAuthorize("hasRole('HOD')")
    public ResponseEntity<?> lockSheet(@PathVariable Long sheetId) {
        oemService.lockSheet(sheetId);
        return ResponseEntity.ok("Marks sheet locked successfully");
    }
}
