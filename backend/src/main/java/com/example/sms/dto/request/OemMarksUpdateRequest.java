package com.example.sms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OemMarksUpdateRequest {
    @NotNull(message = "Sheet ID is required")
    private Long sheetId;

    private List<StudentMarkDto> marks;

    @Data
    public static class StudentMarkDto {
        @NotNull(message = "Student ID is required")
        private Long studentId;
        private Double assessmentScore = 0.0;
        private Double practicalScore = 0.0;
        private Double semesterScore = 0.0;
    }
}
