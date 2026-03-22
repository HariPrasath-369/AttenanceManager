package com.example.sms.repository;

import com.example.sms.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByClassEntityId(Long classId);
    List<Attendance> findByClassEntityIdAndDateBetween(Long classId, LocalDate startDate, LocalDate endDate);
    Optional<Attendance> findByClassEntityIdAndDateAndSession(Long classId, LocalDate date, Attendance.Session session);
    boolean existsByClassEntityIdAndDateAndSession(Long classId, LocalDate date, Attendance.Session session);

    @Query("SELECT a FROM Attendance a LEFT JOIN FETCH a.records WHERE a.classEntity.id = :classId AND a.date = :date AND a.session = :session")
    Optional<Attendance> findByClassEntityIdAndDateAndSessionWithRecords(@Param("classId") Long classId, @Param("date") LocalDate date, @Param("session") Attendance.Session session);
}
