package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String action; // e.g., "UPDATE_MARKS"
    private String entityType; // e.g., "OemRecord"
    private Long entityId;
    private String oldValue;
    private String newValue;

    private LocalDateTime timestamp = LocalDateTime.now();
}
