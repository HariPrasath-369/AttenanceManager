package com.example.sms.service;

import com.example.sms.entity.Hod;
import com.example.sms.entity.Student;
import com.example.sms.entity.Teacher;
import com.example.sms.entity.User;
import com.example.sms.repository.HodRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.repository.TeacherRepository;
import com.example.sms.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {
    private final UserRepository userRepository;
    private final HodRepository hodRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public UserContextService(UserRepository userRepository,
                              HodRepository hodRepository,
                              TeacherRepository teacherRepository,
                              StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.hodRepository = hodRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public User getCurrentUser() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Hod getCurrentHod() {
        User user = getCurrentUser();
        return hodRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Logged in user is not an HOD"));
    }

    public Teacher getCurrentTeacher() {
        User user = getCurrentUser();
        return teacherRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Logged in user is not a Teacher"));
    }

    public Student getCurrentStudent() {
        User user = getCurrentUser();
        return studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Logged in user is not a Student"));
    }
}
