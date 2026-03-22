package com.example.sms.controller;

import com.example.sms.dto.response.TimetableResponse;
import com.example.sms.service.TimetableService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT', 'HOD', 'CLASS_ADVISOR')")
public class TimetableController {
    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<TimetableResponse>> getTimetableForClass(@PathVariable Long classId) {
        return ResponseEntity.ok(timetableService.getTimetableForClass(classId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TimetableResponse>> getTimetableForTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(timetableService.getTimetableForTeacher(teacherId));
    }
}
