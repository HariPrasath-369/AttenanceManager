package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "oem_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OemRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sheet_id")
    private OemSheet sheet;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Double assessmentScore = 0.0;
    private Double practicalScore = 0.0;
    private Double semesterScore = 0.0;
    private Double totalScore = 0.0; // Total
    private String grade;

    private LocalDateTime updatedAt = LocalDateTime.now();
}
