package com.example.sms.repository;

import com.example.sms.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    List<Semester> findByClassEntityId(Long classId);
    List<Semester> findByStatus(Semester.Status status);
    Optional<Semester> findByClassEntityIdAndYearAndSemesterNumber(Long classId, Integer year, Integer semesterNumber);

    @Query("SELECT s FROM Semester s WHERE s.classEntity.id = :classId AND s.status = 'ACTIVE'")
    Optional<Semester> findActiveSemesterByClassId(@Param("classId") Long classId);
}
