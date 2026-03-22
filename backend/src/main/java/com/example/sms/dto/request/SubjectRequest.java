package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class SubjectRequest {
    @NotBlank
    private String name;

    private String code;

    @NotNull
    private Long departmentId;
}
