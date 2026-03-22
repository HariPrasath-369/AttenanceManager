package com.example.sms.controller;

import com.example.sms.dto.request.RequestDto;
import com.example.sms.dto.response.*;
import com.example.sms.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {
    private final AttendanceService attendanceService;
    private final WorkflowService workflowService;
    private final UserContextService userContextService;
    private final TimetableService timetableService;
    private final OemService oemService;
    private final MaterialService materialService;
    private final SuggestionService suggestionService;
    private final StudentService studentService;

    public StudentController(AttendanceService attendanceService,
                             WorkflowService workflowService,
                             UserContextService userContextService,
                             TimetableService timetableService,
                             EvaluationService evaluationService,
                             OemService oemService,
                             MaterialService materialService,
                             SuggestionService suggestionService,
                             StudentService studentService) {
        this.attendanceService = attendanceService;
        this.workflowService = workflowService;
        this.userContextService = userContextService;
        this.timetableService = timetableService;
        this.oemService = oemService;
        this.materialService = materialService;
        this.suggestionService = suggestionService;
        this.studentService = studentService;
    }

    @GetMapping("/attendance")
    public ResponseEntity<com.example.sms.dto.response.StudentAttendanceSummaryResponse> getMyAttendance(@RequestParam String startDate,
                                                 @RequestParam String endDate) {
        Long studentId = userContextService.getCurrentStudent().getId();
        return ResponseEntity.ok(attendanceService.getStudentAttendanceSummary(studentId, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }

    @PostMapping("/requests")
    public ResponseEntity<RequestResponse> submitRequest(@Valid @RequestBody RequestDto requestDto) {
        Long userId = userContextService.getCurrentUser().getId();
        return ResponseEntity.ok(workflowService.createLeaveOrOdRequest(requestDto, userId));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RequestResponse>> getMyRequests() {
        Long studentId = userContextService.getCurrentStudent().getId();
        return ResponseEntity.ok(workflowService.getRequestsByStudent(studentId));
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentResponse> getMyProfile() {
        return ResponseEntity.ok(studentService.getStudentProfileForCurrentStudent());
    }

    @GetMapping("/timetable")
    public ResponseEntity<List<TimetableResponse>> getMyTimetable() {
        Long classId = userContextService.getCurrentStudent().getClassEntity().getId();
        return ResponseEntity.ok(timetableService.getTimetableForClass(classId));
    }

    @GetMapping("/marks")
    public ResponseEntity<StudentMarksResponse> getMyMarks() {
        Long studentId = userContextService.getCurrentStudent().getId();
        return ResponseEntity.ok(oemService.getStudentMarks(studentId));
    }

    @GetMapping("/materials")
    public ResponseEntity<List<MaterialResponse>> getMyMaterials() {
        Long classId = userContextService.getCurrentStudent().getClassEntity().getId();
        return ResponseEntity.ok(materialService.getMaterialsForClass(classId));
    }

    @PostMapping("/suggestions")
    public ResponseEntity<?> submitSuggestion(@RequestBody String content) {
        suggestionService.submitSuggestion(content);
        return ResponseEntity.ok("Suggestion submitted to HOD");
    }
}
