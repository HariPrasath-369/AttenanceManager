package com.example.sms.repository;

import com.example.sms.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByDepartmentId(Long departmentId);
    Optional<Subject> findByNameAndDepartmentId(String name, Long departmentId);

    @Query("SELECT s FROM Subject s WHERE s.department.id = :deptId ORDER BY s.name")
    List<Subject> findAllByDepartmentOrderByName(@Param("deptId") Long deptId);
}
