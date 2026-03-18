package com.attendance.backend.service;

import com.attendance.backend.entity.Attendance;
import com.attendance.backend.entity.SessionType;
import com.attendance.backend.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance markAttendance(Attendance attendance) {
        Optional<Attendance> existing = attendanceRepository.findByStudentIdAndDateAndSession(
                attendance.getStudentId(), attendance.getDate(), attendance.getSession()
        );

        if (existing.isPresent()) {
            Attendance record = existing.get();
            record.setStatus(attendance.getStatus());
            return attendanceRepository.save(record);
        }
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public List<Attendance> getAttendanceByStudentId(String studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}
