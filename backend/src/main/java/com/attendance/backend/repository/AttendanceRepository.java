package com.attendance.backend.repository;

import com.attendance.backend.entity.Attendance;
import com.attendance.backend.entity.SessionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByStudentId(String studentId);
    Optional<Attendance> findByStudentIdAndDateAndSession(String studentId, LocalDate date, SessionType session);
}
