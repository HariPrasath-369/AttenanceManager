package com.example.sms.repository;

import com.example.sms.entity.Teacher;
import com.example.sms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserId(Long userId);
    Optional<Teacher> findByUser(User user);
    Optional<Teacher> findByUser_Username(String username);
    List<Teacher> findByDepartmentId(Long departmentId);
    long countByDepartmentId(Long departmentId);
    List<Teacher> findByAdvisorClassId(Long classId);
    boolean existsByAdvisorClassId(Long classId);

    @Query("SELECT t FROM Teacher t WHERE t.department.id = :deptId")
    List<Teacher> findTeachersByDepartmentId(@Param("deptId") Long deptId);

    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.subjects WHERE t.id = :id")
    Optional<Teacher> findByIdWithSubjects(@Param("id") Long id);
}
