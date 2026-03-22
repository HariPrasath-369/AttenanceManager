package com.example.sms.controller;

import com.example.sms.dto.request.*;
import com.example.sms.dto.response.*;
import com.example.sms.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/principal")
@PreAuthorize("hasRole('PRINCIPAL')")
public class PrincipalController {
    private final DepartmentService departmentService;
    private final HodService hodService;
    private final TeacherService teacherService;
    private final EvaluationService evaluationService;
    private final AnalyticsService analyticsService;

    public PrincipalController(DepartmentService departmentService,
                               HodService hodService,
                               TeacherService teacherService,
                               EvaluationService evaluationService,
                               AnalyticsService analyticsService) {
        this.departmentService = departmentService;
        this.hodService = hodService;
        this.teacherService = teacherService;
        this.evaluationService = evaluationService;
        this.analyticsService = analyticsService;
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PostMapping("/departments")
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.createDepartment(request));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully");
    }

    @GetMapping("/hods")
    public ResponseEntity<List<HodResponse>> getAllHods() {
        return ResponseEntity.ok(hodService.getAllHods());
    }

    @GetMapping("/hods/{id}")
    public ResponseEntity<HodResponse> getHodById(@PathVariable Long id) {
        return ResponseEntity.ok(hodService.getHodById(id));
    }

    @PostMapping("/hods")
    public ResponseEntity<HodResponse> createHod(@Valid @RequestBody HodRequest request) {
        return ResponseEntity.ok(hodService.createHod(request));
    }

    @PutMapping("/hods/{id}")
    public ResponseEntity<HodResponse> updateHod(@PathVariable Long id, @Valid @RequestBody HodRequest request) {
        return ResponseEntity.ok(hodService.updateHod(id, request));
    }

    @DeleteMapping("/hods/{id}")
    public ResponseEntity<?> deleteHod(@PathVariable Long id) {
        hodService.deleteHod(id);
        return ResponseEntity.ok("HOD deleted successfully");
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getPrincipalDashboard() {
        return ResponseEntity.ok(analyticsService.getPrincipalDashboard());
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherResponse>> getTeachersByDepartment(@RequestParam Long departmentId) {
        return ResponseEntity.ok(teacherService.getTeachersByDepartment(departmentId));
    }

    @GetMapping("/performance/department/{deptId}")
    public ResponseEntity<List<ClassPerformanceResponse>> getPerformanceByDepartment(@PathVariable Long deptId) {
        // We reuse HOD's performance logic but for a specific department
        return ResponseEntity.ok(evaluationService.getPerformanceByDepartment(deptId));
    }
}
