package com.attendance.backend.controller;

import com.attendance.backend.entity.Message;
import com.attendance.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/public/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @PostMapping("/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.addMessage(message));
    }

    @DeleteMapping("/messages/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteMessage(@PathVariable String id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
