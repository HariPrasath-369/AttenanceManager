package com.example.sms.repository;

import com.example.sms.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByStudentId(Long studentId);
    List<Request> findByTeacherId(Long teacherId);
    List<Request> findByStatus(Request.Status status);
    List<Request> findByType(Request.RequestType type);
    List<Request> findByTeacherIdAndStatus(Long teacherId, Request.Status status);

    @Query("SELECT r FROM Request r WHERE r.student.classEntity.id = :classId AND r.type = :type AND r.status = :status")
    List<Request> findByClassIdAndTypeAndStatus(@Param("classId") Long classId, @Param("type") Request.RequestType type, @Param("status") Request.Status status);
}
