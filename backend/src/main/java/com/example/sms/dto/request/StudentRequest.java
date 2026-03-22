package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class StudentRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    private String password;

    @NotBlank
    private String fullName;

    @NotBlank
    private String rollNumber;

    private Long classId;

    private LocalDate dateOfBirth;

    @Pattern(regexp = "^[0-9]{10}$")
    private String parentContact;

    @Email
    private String parentEmail;
}
