package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
public class MarksRequest {
    @NotNull
    private Long sheetId; // OemSheet ID

    @NotEmpty
    private List<StudentMarks> marks;

    @Data
    public static class StudentMarks {
        @NotNull
        private Long studentId;
        private Double assessmentMarks;
        private Double practicalMarks;
        private Double semesterMarks;
    }
}
