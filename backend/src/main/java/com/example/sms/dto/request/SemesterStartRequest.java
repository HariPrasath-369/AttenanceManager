package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class SemesterStartRequest {
    @NotNull
    private Long classId;

    @NotNull
    private Integer year;

    @NotNull
    private Integer semesterNumber;
}
