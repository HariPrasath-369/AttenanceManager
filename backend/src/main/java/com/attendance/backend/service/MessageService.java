package com.attendance.backend.service;

import com.attendance.backend.entity.Message;
import com.attendance.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message addMessage(Message message) {
        message.setCreatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public void deleteMessage(String id) {
        messageRepository.deleteById(id);
    }
}
