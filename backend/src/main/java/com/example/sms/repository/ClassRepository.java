package com.example.sms.repository;

import com.example.sms.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByDepartmentId(Long departmentId);
    List<ClassEntity> findByYearAndSemester(Integer year, Integer semester);
    Optional<ClassEntity> findByNameAndDepartmentId(String name, Long departmentId);

    @Query("SELECT c FROM ClassEntity c LEFT JOIN FETCH c.classSubjects WHERE c.id = :id")
    Optional<ClassEntity> findByIdWithSubjects(@Param("id") Long id);

    @Query("SELECT c FROM ClassEntity c LEFT JOIN FETCH c.students WHERE c.id = :id")
    Optional<ClassEntity> findByIdWithStudents(@Param("id") Long id);
}
