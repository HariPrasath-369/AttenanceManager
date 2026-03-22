package com.example.sms.service;

import com.example.sms.dto.request.TimetableRequest;
import com.example.sms.dto.response.TimetableResponse;
import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final UserContextService userContextService;

    public TimetableService(TimetableRepository timetableRepository,
                            ClassRepository classRepository,
                            SubjectRepository subjectRepository,
                            TeacherRepository teacherRepository,
                            UserContextService userContextService) {
        this.timetableRepository = timetableRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.userContextService = userContextService;
    }

    @Transactional
    public TimetableResponse createTimetableEntry(TimetableRequest request) {
        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        int count = timetableRepository.findByClassEntityIdAndDayOfWeek(request.getClassId(), request.getDayOfWeek()).size();
        if (count >= 2) {
            throw new RuntimeException("Only 2 subjects per day allowed");
        }
        Timetable timetable = new Timetable();
        timetable.setClassEntity(classEntity);
        timetable.setDayOfWeek(request.getDayOfWeek());
        timetable.setPeriod(request.getPeriod());
        timetable.setSubject(subject);
        timetable.setTeacher(teacher);
        timetable.setVenue(request.getVenue());
        timetable = timetableRepository.save(timetable);
        return mapToResponse(timetable);
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getTimetableForClass(Long classId) {
        return timetableRepository.findByClassEntityIdOrdered(classId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getTimetableForCurrentTeacher() {
        Teacher teacher = userContextService.getCurrentTeacher();
        return getTimetableForTeacher(teacher.getId());
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getTimetableForTeacher(Long teacherId) {
        return timetableRepository.findByTeacherIdOrdered(teacherId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTimetableEntry(Long id) {
        timetableRepository.deleteById(id);
    }

    private TimetableResponse mapToResponse(Timetable tt) {
        TimetableResponse response = new TimetableResponse();
        response.setId(tt.getId());
        response.setDayOfWeek(tt.getDayOfWeek());
        response.setPeriod(tt.getPeriod());
        response.setClassId(tt.getClassEntity().getId());
        response.setClassName(tt.getClassEntity().getName());
        response.setSubjectCode(tt.getSubject().getCode());
        response.setSubjectName(tt.getSubject().getName());
        response.setTeacherName(tt.getTeacher().getUser().getFullName());
        response.setVenue(tt.getVenue());
        return response;
    }
}
