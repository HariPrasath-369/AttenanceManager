package com.example.sms.repository;

import com.example.sms.entity.ClassSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long> {
    List<ClassSubject> findByClassEntityId(Long classId);
    List<ClassSubject> findByTeacherId(Long teacherId);
    List<ClassSubject> findBySubjectId(Long subjectId);
    Optional<ClassSubject> findByClassEntityIdAndSubjectId(Long classId, Long subjectId);

    @Query("SELECT cs FROM ClassSubject cs LEFT JOIN FETCH cs.teacher WHERE cs.classEntity.id = :classId")
    List<ClassSubject> findByClassEntityIdWithTeacher(@Param("classId") Long classId);

    @Query("SELECT cs FROM ClassSubject cs LEFT JOIN FETCH cs.subject WHERE cs.teacher.id = :teacherId")
    List<ClassSubject> findByTeacherIdWithSubject(@Param("teacherId") Long teacherId);
}
