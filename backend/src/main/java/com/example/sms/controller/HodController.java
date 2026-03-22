package com.example.sms.controller;

import com.example.sms.dto.request.*;
import com.example.sms.dto.response.*;
import com.example.sms.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hod")
@PreAuthorize("hasRole('HOD')")
public class HodController {
    private final TeacherService teacherService;
    private final AcademicService academicService;
    private final TimetableService timetableService;
    private final WorkflowService workflowService;
    private final AnalyticsService analyticsService;
    private final EvaluationService evaluationService;
    private final SuggestionService suggestionService;
    private final UserContextService userContextService;

    public HodController(TeacherService teacherService,
                         AcademicService academicService,
                         TimetableService timetableService,
                         WorkflowService workflowService,
                         AnalyticsService analyticsService,
                         EvaluationService evaluationService,
                         SuggestionService suggestionService,
                         UserContextService userContextService) {
        this.teacherService = teacherService;
        this.academicService = academicService;
        this.timetableService = timetableService;
        this.workflowService = workflowService;
        this.analyticsService = analyticsService;
        this.evaluationService = evaluationService;
        this.suggestionService = suggestionService;
        this.userContextService = userContextService;
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherResponse>> getTeachers(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(teacherService.getTeachersForCurrentHod());
    }

    @PostMapping("/teachers")
    public ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody TeacherRequest request,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(teacherService.createTeacher(request));
    }

    @PutMapping("/teachers/{id}")
    public ResponseEntity<TeacherResponse> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, request));
    }

    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok("Teacher deleted successfully");
    }

    @PostMapping("/classes")
    public ResponseEntity<ClassResponse> createClass(@Valid @RequestBody ClassRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(academicService.createClass(request));
    }

    @GetMapping("/classes")
    public ResponseEntity<List<ClassResponse>> getClasses(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(academicService.getClassesForCurrentHod());
    }

    @PostMapping("/subjects")
    public ResponseEntity<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest request,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(academicService.createSubject(request));
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectResponse>> getSubjects(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(academicService.getSubjectsForCurrentHod());
    }

    @PostMapping("/class-subjects")
    public ResponseEntity<ClassSubjectResponse> assignTeacherToSubject(@Valid @RequestBody ClassSubjectRequest request) {
        return ResponseEntity.ok(academicService.assignTeacherToSubject(request));
    }

    @PutMapping("/classes/{classId}/advisor")
    public ResponseEntity<ClassResponse> assignClassAdvisor(@PathVariable Long classId, @RequestParam Long teacherId) {
        return ResponseEntity.ok(academicService.assignClassAdvisor(classId, teacherId));
    }

    @PostMapping("/timetable")
    public ResponseEntity<TimetableResponse> createTimetableEntry(@Valid @RequestBody TimetableRequest request) {
        return ResponseEntity.ok(timetableService.createTimetableEntry(request));
    }

    @GetMapping("/timetable/class/{classId}")
    public ResponseEntity<List<TimetableResponse>> getTimetableForClass(@PathVariable Long classId) {
        return ResponseEntity.ok(timetableService.getTimetableForClass(classId));
    }

    @DeleteMapping("/timetable/{id}")
    public ResponseEntity<?> deleteTimetableEntry(@PathVariable Long id) {
        timetableService.deleteTimetableEntry(id);
        return ResponseEntity.ok("Timetable entry deleted");
    }

    @GetMapping("/semester-requests")
    public ResponseEntity<List<RequestResponse>> getPendingSemesterRequests() {
        Long deptId = userContextService.getCurrentHod().getDepartment().getId();
        return ResponseEntity.ok(workflowService.getPendingSemesterRequestsForDepartment(deptId));
    }

    @PutMapping("/semester-requests/{requestId}/approve")
    public ResponseEntity<?> approveSemesterStart(@PathVariable Long requestId) {
        Long hodId = userContextService.getCurrentHod().getId();
        workflowService.approveSemesterStart(requestId, hodId);
        return ResponseEntity.ok("Semester start approved");
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getHodDashboard() {
        Long hodId = userContextService.getCurrentHod().getId();
        return ResponseEntity.ok(analyticsService.getHodDashboard(hodId));
    }
    @GetMapping("/performance")
    public ResponseEntity<List<ClassPerformanceResponse>> getPerformance() {
        return ResponseEntity.ok(evaluationService.getPerformanceForCurrentHod());
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<SuggestionResponse>> getSuggestions() {
        return ResponseEntity.ok(suggestionService.getSuggestionsForCurrentHod());
    }
}
