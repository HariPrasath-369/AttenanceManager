package com.attendance.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "settings")
public class Settings {
    @Id
    private String id;
    private List<String> holidays; // array of dates as strings, or could be LocalDate
    private String closeTime; // e.g., "16:00"
}
