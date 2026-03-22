package com.example.sms.controller;

import com.example.sms.dto.response.ClassResponse;
import com.example.sms.dto.response.SubjectResponse;
import com.example.sms.dto.response.TimetableResponse;
import com.example.sms.service.AcademicService;
import com.example.sms.service.TimetableService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {
    private final AcademicService academicService;
    private final TimetableService timetableService;

    public TeacherController(AcademicService academicService,
                             TimetableService timetableService) {
        this.academicService = academicService;
        this.timetableService = timetableService;
    }

    @GetMapping("/classes")
    public ResponseEntity<List<ClassResponse>> getMyClasses() {
        return ResponseEntity.ok(academicService.getClassesForCurrentTeacher());
    }

    @GetMapping("/timetable")
    public ResponseEntity<List<TimetableResponse>> getMyTimetable() {
        return ResponseEntity.ok(timetableService.getTimetableForCurrentTeacher());
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectResponse>> getMySubjects() {
        return ResponseEntity.ok(academicService.getSubjectsForCurrentTeacher());
    }
}
