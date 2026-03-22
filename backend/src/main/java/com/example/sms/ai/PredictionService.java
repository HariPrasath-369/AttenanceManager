package com.example.sms.ai;

import com.example.sms.entity.Student;
import com.example.sms.repository.OemRecordRepository;
import com.example.sms.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionService {
    private final StudentRepository studentRepository;
    private final OemRecordRepository oemRecordRepository;

    public PredictionService(StudentRepository studentRepository,
                             OemRecordRepository oemRecordRepository) {
        this.studentRepository = studentRepository;
        this.oemRecordRepository = oemRecordRepository;
    }

    // Simple rule-based prediction (in real scenario use ML model)
    public String predictFailureRisk(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return "UNKNOWN";
        // Get marks from last semester
        List<Double> marks = oemRecordRepository.findRecentMarksByStudentId(studentId, 5);
        if (marks.isEmpty()) return "LOW_DATA";
        double average = marks.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        if (average < 40) return "HIGH";
        else if (average < 60) return "MEDIUM";
        else return "LOW";
    }
}
