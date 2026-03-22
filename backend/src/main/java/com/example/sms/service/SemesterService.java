package com.example.sms.service;

import com.example.sms.dto.request.SemesterStartRequest;
import com.example.sms.dto.response.SemesterResponse;
import com.example.sms.entity.ClassEntity;
import com.example.sms.entity.Semester;
import com.example.sms.repository.ClassRepository;
import com.example.sms.repository.SemesterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemesterService {
    private final SemesterRepository semesterRepository;
    private final ClassRepository classRepository;

    public SemesterService(SemesterRepository semesterRepository,
                           ClassRepository classRepository) {
        this.semesterRepository = semesterRepository;
        this.classRepository = classRepository;
    }

    @Transactional
    public SemesterResponse startSemester(SemesterStartRequest request) {
        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        if (semesterRepository.findActiveSemesterByClassId(request.getClassId()).isPresent()) {
            throw new RuntimeException("Active semester already exists for this class");
        }
        Semester semester = new Semester();
        semester.setClassEntity(classEntity);
        semester.setYear(request.getYear());
        semester.setSemesterNumber(request.getSemesterNumber());
        semester.setStatus(Semester.Status.ACTIVE);
        semester.setStartDate(LocalDate.now());
        semester = semesterRepository.save(semester);
        return mapToResponse(semester);
    }

    @Transactional
    public void endSemester(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Semester not found"));
        semester.setStatus(Semester.Status.ENDED);
        semester.setEndDate(LocalDate.now());
        semesterRepository.save(semester);
    }

    @Transactional(readOnly = true)
    public List<SemesterResponse> getSemestersForClass(Long classId) {
        return semesterRepository.findByClassEntityId(classId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SemesterResponse mapToResponse(Semester semester) {
        SemesterResponse response = new SemesterResponse();
        response.setId(semester.getId());
        response.setClassName(semester.getClassEntity().getName());
        response.setYear(semester.getYear());
        response.setSemesterNumber(semester.getSemesterNumber());
        response.setStatus(semester.getStatus().name());
        response.setStartDate(semester.getStartDate());
        response.setEndDate(semester.getEndDate());
        return response;
    }
}
