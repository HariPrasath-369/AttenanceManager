package com.example.sms.repository;

import com.example.sms.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByClassSubjectId(Long classSubjectId);
    List<Material> findByUploadedById(Long teacherId);
}
