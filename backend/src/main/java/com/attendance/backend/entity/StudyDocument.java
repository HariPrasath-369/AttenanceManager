package com.attendance.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "documents")
public class StudyDocument {
    @Id
    private String id;
    private String title;
    private String fileUrl;
    private LocalDateTime uploadedAt;
}
