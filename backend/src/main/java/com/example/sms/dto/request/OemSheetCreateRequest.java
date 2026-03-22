package com.example.sms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OemSheetCreateRequest {
    @NotNull(message = "Class ID is required")
    private Long classId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotNull(message = "Semester is required")
    private Integer semester;

    private String sheetType = "Semester"; // Default to Semester if not provided
    private Integer maxMarks = 100;
}
