package com.example.sms.repository;

import com.example.sms.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByClassEntityId(Long classId);
    List<Timetable> findByTeacherId(Long teacherId);
    List<Timetable> findByClassEntityIdAndDayOfWeek(Long classId, Integer dayOfWeek);

    @Query("SELECT t FROM Timetable t WHERE t.classEntity.id = :classId ORDER BY t.dayOfWeek, t.period")
    List<Timetable> findByClassEntityIdOrdered(@Param("classId") Long classId);

    @Query("SELECT t FROM Timetable t WHERE t.teacher.id = :teacherId ORDER BY t.dayOfWeek, t.period")
    List<Timetable> findByTeacherIdOrdered(@Param("teacherId") Long teacherId);
}
