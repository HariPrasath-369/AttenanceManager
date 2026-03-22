package com.example.sms.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RequestResponse {
    private Long id;
    private String type;
    private String description;
    private String status;
    private String studentName;
    private String studentRollNumber;
    private LocalDateTime createdAt;
    private String comments;
}
