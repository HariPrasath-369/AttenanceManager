package com.example.sms.service;

import com.example.sms.dto.request.AttendanceRequest;
import com.example.sms.dto.response.AttendanceResponse;
import com.example.sms.dto.response.StudentAttendanceSummaryResponse;
import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final AuditService auditService;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             AttendanceRecordRepository attendanceRecordRepository,
                             ClassRepository classRepository,
                             StudentRepository studentRepository,
                             AuditService auditService) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
        this.auditService = auditService;
    }

    @Transactional
    public AttendanceResponse takeAttendance(AttendanceRequest request) {
        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        
        Attendance.Session session = Attendance.Session.valueOf(request.getSession());

        Attendance attendance = attendanceRepository.findByClassEntityIdAndDateAndSession(request.getClassId(), request.getDate(), session)
                .orElse(new Attendance());
        
        if (attendance.getId() != null && attendance.getIsLocked()) {
            throw new RuntimeException("Attendance for this date and session is locked and cannot be changed");
        }
        
        attendance.setClassEntity(classEntity);
        attendance.setDate(request.getDate());
        attendance.setSession(session);
        
        if (attendance.getId() != null && attendance.getRecords() != null) {
            attendanceRecordRepository.deleteAll(attendance.getRecords());
            attendance.getRecords().clear();
        }
        
        final Attendance finalAttendance = attendance;
        List<AttendanceRecord> records = request.getRecords().stream()
                .map(rec -> {
                    Student student = studentRepository.findById(rec.getStudentId())
                            .orElseThrow(() -> new RuntimeException("Student not found"));
                    AttendanceRecord record = new AttendanceRecord();
                    record.setAttendance(finalAttendance);
                    record.setStudent(student);
                    record.setStatus(AttendanceRecord.Status.valueOf(rec.getStatus()));
                    return record;
                }).collect(Collectors.toList());
                
        attendance.setRecords(records);
        attendance = attendanceRepository.save(attendance);
        
        return mapToResponse(attendance);
    }

    @Transactional
    public void lockAttendance(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));
        attendance.setIsLocked(true);
        attendanceRepository.save(attendance);
        auditService.log("LOCK_ATTENDANCE", "Attendance", attendance.getId(), "UNLOCKED", "LOCKED");
    }

    @Transactional(readOnly = true)
    public AttendanceResponse getAttendanceByDateAndSession(Long classId, LocalDate date, String sessionStr) {
        Attendance.Session session = Attendance.Session.valueOf(sessionStr);
        Attendance attendance = attendanceRepository.findByClassEntityIdAndDateAndSessionWithRecords(classId, date, session)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));
        return mapToResponse(attendance);
    }
    
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByClassAndDateRange(Long classId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceRepository.findByClassEntityIdAndDateBetween(classId, startDate, endDate);
        return attendances.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentAttendanceSummaryResponse getStudentAttendanceSummary(Long studentId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceRecord> records = attendanceRecordRepository.findByStudentIdAndDateRange(studentId, startDate, endDate);
        int totalSessions = records.size();
        
        long presentCount = records.stream()
                .filter(r -> r.getStatus() == AttendanceRecord.Status.PRESENT || r.getStatus() == AttendanceRecord.Status.OD)
                .count();
                
        StudentAttendanceSummaryResponse response = new StudentAttendanceSummaryResponse();
        response.setStudentId(studentId);
        response.setTotalDays(totalSessions);
        response.setPresentDays((int) presentCount);
        
        if (totalSessions == 0) {
            response.setPercentage(0.0);
        } else {
            response.setPercentage((double) presentCount / totalSessions * 100);
        }
        return response;
    }

    private AttendanceResponse mapToResponse(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setDate(attendance.getDate());
        response.setSession(attendance.getSession() != null ? attendance.getSession().name() : null);
        response.setIsLocked(attendance.getIsLocked());
        
        List<AttendanceResponse.StudentAttendance> studentAttendances = attendance.getRecords().stream()
                .map(r -> {
                    AttendanceResponse.StudentAttendance sa = new AttendanceResponse.StudentAttendance();
                    sa.setStudentId(r.getStudent().getId());
                    sa.setStudentName(r.getStudent().getUser().getFullName());
                    sa.setStatus(r.getStatus().name());
                    return sa;
                }).collect(Collectors.toList());
                
        response.setStudentAttendances(studentAttendances);
        return response;
    }
}
