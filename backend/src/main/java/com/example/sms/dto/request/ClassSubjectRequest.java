package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ClassSubjectRequest {
    @NotNull
    private Long classId;

    @NotNull
    private Long subjectId;

    @NotNull
    private Long teacherId;
}
