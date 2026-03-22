package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class TeacherRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    private String password;

    @NotBlank
    private String fullName;

    private Long userId; // For existing user link
    private Long departmentId;
    private String joiningDate;
}
