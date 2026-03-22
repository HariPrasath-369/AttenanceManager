package com.example.sms.dto.response;

import lombok.Data;
import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Boolean isActive;
    private Set<String> roles;
}
