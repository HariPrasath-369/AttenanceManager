package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class RequestDto {
    @NotNull
    private String type; // LEAVE, OD

    @NotBlank
    private String description;
}
