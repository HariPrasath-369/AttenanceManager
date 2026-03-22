package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Recipient

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // INFO, WARNING, SUCCESS

    private Boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum NotificationType {
        INFO, WARNING, SUCCESS
    }
}
