package com.example.sms.controller;

import com.example.sms.dto.request.AttendanceRequest;
import com.example.sms.dto.response.AttendanceResponse;
import com.example.sms.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@PreAuthorize("hasAnyRole('TEACHER', 'CLASS_ADVISOR', 'HOD', 'PRINCIPAL')")
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> takeAttendance(@Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.takeAttendance(request));
    }

    @GetMapping("/{classId}/{date}/{session}")
    public ResponseEntity<AttendanceResponse> getAttendance(@PathVariable Long classId, @PathVariable String date, @PathVariable String session) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDateAndSession(classId, LocalDate.parse(date), session));
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudentAttendance(@PathVariable Long id, @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceSummary(id, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }

    @GetMapping("/class/{id}/range")
    public ResponseEntity<List<AttendanceResponse>> getClassAttendanceRange(@PathVariable Long id, @RequestParam String start, @RequestParam String end) {
        return ResponseEntity.ok(attendanceService.getAttendanceByClassAndDateRange(id, LocalDate.parse(start), LocalDate.parse(end)));
    }

    @PutMapping("/{id}/lock")
    @PreAuthorize("hasAnyRole('CLASS_ADVISOR', 'HOD')")
    public ResponseEntity<?> lockAttendance(@PathVariable Long id) {
        attendanceService.lockAttendance(id);
        return ResponseEntity.ok("Attendance locked successfully");
    }
}
