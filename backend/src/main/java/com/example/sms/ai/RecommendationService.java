package com.example.sms.ai;

import com.example.sms.entity.Student;
import com.example.sms.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {
    private final StudentRepository studentRepository;

    public RecommendationService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<String> getRecommendations(Long studentId) {
        List<String> recommendations = new ArrayList<>();
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return recommendations;
        // Example: based on performance, suggest extra study
        // In real implementation, use ML model
        recommendations.add("Consider attending remedial classes for Mathematics.");
        recommendations.add("Join study groups to improve conceptual understanding.");
        return recommendations;
    }
}
