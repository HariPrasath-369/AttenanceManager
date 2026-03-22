package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id") // For CA approval
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    private RequestType type; // SEMESTER_START, LEAVE, OD

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // PENDING, APPROVED, REJECTED

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    private String comments;

    public enum RequestType {
        SEMESTER_START, LEAVE, OD
    }

    public enum Status {
        PENDING, APPROVED, REJECTED
    }
}
