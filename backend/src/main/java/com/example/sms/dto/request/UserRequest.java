package com.example.sms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.Set;

@Data
public class UserRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    private String password; // Optional for updates

    @NotBlank
    private String fullName;

    @NotEmpty
    private Set<String> roles;
}
