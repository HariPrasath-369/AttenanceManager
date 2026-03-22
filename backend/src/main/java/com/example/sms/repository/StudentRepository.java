package com.example.sms.repository;

import com.example.sms.entity.Student;
import com.example.sms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByRollNumber(String rollNumber);
    List<Student> findByClassEntityId(Long classId);
    List<Student> findByClassEntityIdOrderByRollNumberAsc(Long classId);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user WHERE s.classEntity.id = :classId")
    List<Student> findByClassEntityIdWithUser(@Param("classId") Long classId);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.classEntity WHERE s.user.id = :userId")
    Optional<Student> findByUserIdWithClass(@Param("userId") Long userId);

    long countByClassEntity_Department_Id(Long deptId);
}
