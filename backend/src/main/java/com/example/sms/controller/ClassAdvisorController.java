package com.example.sms.controller;

import com.example.sms.dto.request.AttendanceRequest;
import com.example.sms.dto.request.SemesterStartRequest;
import com.example.sms.dto.request.StudentRequest;
import com.example.sms.dto.response.*;
import com.example.sms.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/advisor")
@PreAuthorize("hasRole('CLASS_ADVISOR')")
public class ClassAdvisorController {
    private final AttendanceService attendanceService;
    private final WorkflowService workflowService;
    private final SemesterService semesterService;
    private final StudentService studentService;
    private final EvaluationService evaluationService;
    private final AnalyticsService analyticsService;
    private final UserContextService userContextService;

    public ClassAdvisorController(AttendanceService attendanceService,
                                  WorkflowService workflowService,
                                  SemesterService semesterService,
                                  StudentService studentService,
                                  EvaluationService evaluationService,
                                  AnalyticsService analyticsService,
                                  UserContextService userContextService) {
        this.attendanceService = attendanceService;
        this.workflowService = workflowService;
        this.semesterService = semesterService;
        this.studentService = studentService;
        this.evaluationService = evaluationService;
        this.analyticsService = analyticsService;
        this.userContextService = userContextService;
    }

    @PostMapping("/attendance")
    public ResponseEntity<AttendanceResponse> takeAttendance(@Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.takeAttendance(request));
    }

    @GetMapping("/attendance/{classId}/{date}/{session}")
    public ResponseEntity<AttendanceResponse> getAttendance(@PathVariable Long classId, @PathVariable String date, @PathVariable String session) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDateAndSession(classId, LocalDate.parse(date), session));
    }

    @PostMapping("/semester/start")
    public ResponseEntity<SemesterResponse> startSemester(@Valid @RequestBody SemesterStartRequest request) {
        return ResponseEntity.ok(semesterService.startSemester(request));
    }

    @PostMapping("/semester/end/{semesterId}")
    public ResponseEntity<?> endSemester(@PathVariable Long semesterId) {
        semesterService.endSemester(semesterId);
        return ResponseEntity.ok("Semester ended successfully");
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RequestResponse>> getPendingRequests() {
        Long teacherId = userContextService.getCurrentTeacher().getId();
        return ResponseEntity.ok(workflowService.getRequestsByTeacher(teacherId));
    }

    @PutMapping("/requests/{id}/approve")
    public ResponseEntity<RequestResponse> approveRequest(@PathVariable Long id, @RequestParam String comments) {
        Long userId = userContextService.getCurrentUser().getId();
        return ResponseEntity.ok(workflowService.approveRequest(id, userId, comments));
    }

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<RequestResponse> rejectRequest(@PathVariable Long id, @RequestParam String comments) {
        Long userId = userContextService.getCurrentUser().getId();
        return ResponseEntity.ok(workflowService.rejectRequest(id, userId, comments));
    }

    // Student CRUD for Class Advisor
    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getMyStudents() {
        return ResponseEntity.ok(studentService.getStudentsForCurrentAdvisor());
    }

    @PostMapping("/students")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.createStudent(request));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @GetMapping("/performance")
    public ResponseEntity<ClassPerformanceResponse> getMyClassPerformance() {
        return ResponseEntity.ok(evaluationService.getPerformanceForCurrentAdvisor());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getAdvisorDashboard() {
        Long teacherId = userContextService.getCurrentTeacher().getId();
        return ResponseEntity.ok(analyticsService.getClassAdvisorDashboard(teacherId));
    }
}
