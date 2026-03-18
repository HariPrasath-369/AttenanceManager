package com.attendance.backend.config;

import com.attendance.backend.entity.Role;
import com.attendance.backend.entity.Teacher;
import com.attendance.backend.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.name}")
    private String adminName;

    @Override
    public void run(String... args) throws Exception {
        if (teacherRepository.count() == 0) {
            Teacher mainTeacher = Teacher.builder()
                    .name(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.MAIN)
                    .build();
            teacherRepository.save(mainTeacher);
            System.out.println("Default MAIN teacher created from properties.");
        }
    }
}
