package com.example.sms.repository;

import com.example.sms.entity.Hod;
import com.example.sms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HodRepository extends JpaRepository<Hod, Long> {
    Optional<Hod> findByUser(User user);
    Optional<Hod> findByUserId(Long userId);
    Optional<Hod> findByUser_Username(String username);
    List<Hod> findByDepartmentId(Long departmentId);
    boolean existsByDepartmentId(Long departmentId);

    @Query("SELECT h FROM Hod h WHERE h.department.id = :deptId")
    List<Hod> findHodsByDepartmentId(@Param("deptId") Long deptId);
}
