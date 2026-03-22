package com.example.sms.repository;

import com.example.sms.entity.OemRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OemRecordRepository extends JpaRepository<OemRecord, Long> {
    List<OemRecord> findBySheetId(Long sheetId);
    List<OemRecord> findByStudentId(Long studentId);
    Optional<OemRecord> findBySheetIdAndStudentId(Long sheetId, Long studentId);
    List<OemRecord> findBySheet_ClassEntity_IdAndSheet_Subject_Id(Long classId, Long subjectId);

    @Query("SELECT o FROM OemRecord o WHERE o.sheet.classEntity.id = :classId AND o.student.id = :studentId")
    List<OemRecord> findByClassIdAndStudentId(@Param("classId") Long classId, @Param("studentId") Long studentId);

    @Query(value = "SELECT o.total_score FROM oem_records o WHERE o.student_id = :studentId ORDER BY o.id DESC LIMIT :limit", nativeQuery = true)
    List<Double> findRecentMarksByStudentId(@Param("studentId") Long studentId, @Param("limit") int limit);
}
