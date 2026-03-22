package com.example.sms.controller;

import com.example.sms.entity.Notification;
import com.example.sms.service.NotificationService;
import com.example.sms.service.UserContextService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserContextService userContextService;

    public NotificationController(NotificationService notificationService, UserContextService userContextService) {
        this.notificationService = notificationService;
        this.userContextService = userContextService;
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        Long userId = userContextService.getCurrentUser().getId();
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead() {
        Long userId = userContextService.getCurrentUser().getId();
        notificationService.markAllAsReadForUser(userId);
        return ResponseEntity.ok("All notifications marked as read");
    }
}
