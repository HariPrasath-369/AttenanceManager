package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ClassRequest {
    @NotBlank
    private String name;

    @NotNull
    private Long departmentId;

    private Integer year;
    private Integer semester;
    private Integer capacity;

    private Long classAdvisorId; // Teacher ID
}
