package com.example.sms.repository;

import com.example.sms.entity.OemSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OemSheetRepository extends JpaRepository<OemSheet, Long> {
    List<OemSheet> findByClassEntityIdAndSubjectId(Long classId, Long subjectId);
    List<OemSheet> findByClassEntityIdAndSubjectIdAndSheetType(Long classId, Long subjectId, String sheetType);
    Optional<OemSheet> findByClassEntityIdAndSubjectIdAndSheetTypeAndStatusNot(Long classId, Long subjectId, String sheetType, OemSheet.Status status);

    @Query("SELECT o FROM OemSheet o LEFT JOIN FETCH o.records WHERE o.id = :id")
    Optional<OemSheet> findByIdWithRecords(@Param("id") Long id);

    @Query("SELECT o FROM OemSheet o WHERE o.classEntity.id = :classId")
    List<OemSheet> findByClassId(@Param("classId") Long classId);
}
