package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "semesters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    private Integer year;
    private Integer semesterNumber;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // PENDING, ACTIVE, ENDED

    private LocalDate startDate;
    private LocalDate endDate;

    public enum Status {
        PENDING, ACTIVE, ENDED
    }
}
