package com.example.sms.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MaterialResponse {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String fileType;
    private String subjectName;
    private String className;
    private LocalDateTime uploadedAt;
}
