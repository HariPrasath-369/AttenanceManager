package com.attendance.backend.service;

import com.attendance.backend.dto.LoginRequest;
import com.attendance.backend.dto.LoginResponse;
import com.attendance.backend.entity.Teacher;
import com.attendance.backend.exception.UnauthorizedException;
import com.attendance.backend.repository.TeacherRepository;
import com.attendance.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TeacherRepository teacherRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        System.out.println("Login attempt for email: " + loginRequest.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            System.out.println("Authentication successful for: " + loginRequest.getEmail());
        } catch (Exception e) {
            System.err.println("Authentication failed for: " + loginRequest.getEmail() + " - " + e.getMessage());
            throw new UnauthorizedException("Invalid email or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        Teacher teacher = teacherRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        final String jwt = jwtUtil.generateToken(userDetails, teacher.getRole().name());

        return LoginResponse.builder()
                .token(jwt)
                .email(teacher.getEmail())
                .role(teacher.getRole().name())
                .build();
    }
}
