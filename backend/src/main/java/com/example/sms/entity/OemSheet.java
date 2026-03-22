package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oem_sheets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OemSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private Integer semester;

    private String sheetType; // e.g., "Assessment", "Practical", "Semester"
    private Integer maxMarks;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT; // DRAFT, SUBMITTED, LOCKED

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "sheet", cascade = CascadeType.ALL)
    private List<OemRecord> records = new ArrayList<>();

    @OneToMany(mappedBy = "sheet")
    private List<OemAuditLog> auditLogs = new ArrayList<>();

    public enum Status {
        DRAFT, SUBMITTED, LOCKED
    }
}
