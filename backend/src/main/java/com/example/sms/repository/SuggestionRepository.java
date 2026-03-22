package com.example.sms.repository;

import com.example.sms.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByStudentId(Long studentId);
    List<Suggestion> findByHodIdOrderByCreatedAtDesc(Long hodId);
}
