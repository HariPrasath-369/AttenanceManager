package com.example.sms.repository;

import com.example.sms.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
    List<Department> findAllByOrderByNameAsc();
    Optional<Department> findByHods_Id(Long hodId);
}
