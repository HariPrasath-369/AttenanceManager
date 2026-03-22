package com.example.sms.ai;

import com.example.sms.entity.AttendanceRecord;
import com.example.sms.repository.AttendanceRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnomalyDetectionService {
    private final AttendanceRecordRepository attendanceRecordRepository;

    public AnomalyDetectionService(AttendanceRecordRepository attendanceRecordRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    public boolean isAttendanceAnomaly(Long studentId, LocalDate date) {
        // Check if student's attendance pattern deviates significantly from class average
        // Simplified: if student was absent for 3+ consecutive days
        List<AttendanceRecord> recentRecords = attendanceRecordRepository
                .findByStudentIdAndDateRange(studentId, date.minusDays(7), date);
        long absences = recentRecords.stream()
                .filter(rec -> rec.getStatus() == AttendanceRecord.Status.ABSENT)
                .count();
        return absences >= 3;
    }
}
