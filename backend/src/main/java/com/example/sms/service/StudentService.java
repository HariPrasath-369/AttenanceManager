package com.example.sms.service;

import com.example.sms.dto.request.StudentRequest;
import com.example.sms.dto.response.StudentResponse;
import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserContextService userContextService;

    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          UserContextService userContextService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userContextService = userContextService;
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        // Find advisor's class if current user is TEACHER
        Teacher advisor = userContextService.getCurrentTeacher();
        ClassEntity classEntity = advisor.getAdvisorClass();
        if (classEntity == null) {
            throw new RuntimeException("Teacher is not assigned as a Class Advisor");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword() != null ? request.getPassword() : "Student@123"));
        user.setFullName(request.getFullName());
        user.setIsActive(true);

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("STUDENT role not found"));
        user.setRoles(Set.of(studentRole));
        user = userRepository.save(user);

        Student student = new Student();
        student.setUser(user);
        student.setRollNumber(request.getRollNumber());
        student.setClassEntity(classEntity);
        student.setDateOfBirth(request.getDateOfBirth());
        student.setParentContact(request.getParentContact());
        student.setParentEmail(request.getParentEmail());
        
        return mapToResponse(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsForCurrentAdvisor() {
        Teacher advisor = userContextService.getCurrentTeacher();
        ClassEntity classEntity = advisor.getAdvisorClass();
        if (classEntity == null) {
            return List.of();
        }
        return studentRepository.findByClassEntityId(classEntity.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Security check: CA can only update their own students
        Teacher advisor = userContextService.getCurrentTeacher();
        if (!student.getClassEntity().getId().equals(advisor.getAdvisorClass().getId())) {
            throw new RuntimeException("Unauthorized to update student in another class");
        }

        User user = student.getUser();
        user.setFullName(request.getFullName());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);

        student.setRollNumber(request.getRollNumber());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setParentContact(request.getParentContact());
        student.setParentEmail(request.getParentEmail());

        return mapToResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Teacher advisor = userContextService.getCurrentTeacher();
        if (!student.getClassEntity().getId().equals(advisor.getAdvisorClass().getId())) {
            throw new RuntimeException("Unauthorized to delete student in another class");
        }

        studentRepository.delete(student);
        userRepository.delete(student.getUser());
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentProfileForCurrentStudent() {
        return mapToResponse(userContextService.getCurrentStudent());
    }

    private StudentResponse mapToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFullName(student.getUser().getFullName());
        response.setEmail(student.getUser().getEmail());
        response.setRollNumber(student.getRollNumber());
        response.setClassName(student.getClassEntity().getName());
        response.setDepartmentName(student.getClassEntity().getDepartment().getName());
        response.setDateOfBirth(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null);
        response.setParentContact(student.getParentContact());
        response.setParentEmail(student.getParentEmail());
        return response;
    }
}
