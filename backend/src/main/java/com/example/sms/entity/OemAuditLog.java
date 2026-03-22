package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "oem_audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OemAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sheet_id")
    private OemSheet sheet;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Double oldValue;
    private Double newValue;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    private LocalDateTime modifiedAt = LocalDateTime.now();
}
