package com.example.sms.dto.response;

import lombok.Data;
import java.util.Set;

@Data
public class JwtResponse {
    private String token;
    private Long id;
    private String refreshToken;
    private String username;
    private String email;
    private Set<String> roles;

    public JwtResponse(String token, String refreshToken, Long id, String username, String email, Set<String> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
