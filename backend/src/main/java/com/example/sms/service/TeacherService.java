package com.example.sms.service;

import com.example.sms.dto.request.TeacherRequest;
import com.example.sms.dto.response.TeacherResponse;
import com.example.sms.entity.Department;
import com.example.sms.entity.Teacher;
import com.example.sms.entity.User;
import com.example.sms.repository.DepartmentRepository;
import com.example.sms.repository.TeacherRepository;
import com.example.sms.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserContextService userContextService;

    public TeacherService(TeacherRepository teacherRepository,
                          UserRepository userRepository,
                          DepartmentRepository departmentRepository,
                          UserContextService userContextService) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.userContextService = userContextService;
    }

    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        if (teacherRepository.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException("User is already a teacher");
        }
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setDepartment(dept);
        teacher.setJoiningDate(request.getJoiningDate());
        teacher = teacherRepository.save(teacher);
        return mapToResponse(teacher);
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> getTeachersForCurrentHod() {
        Long deptId = userContextService.getCurrentHod().getDepartment().getId();
        return teacherRepository.findByDepartmentId(deptId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> getTeachersByDepartment(Long deptId) {
        return teacherRepository.findByDepartmentId(deptId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return mapToResponse(teacher);
    }

    @Transactional
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            teacher.setDepartment(dept);
        }
        teacher.setJoiningDate(request.getJoiningDate());
        teacher = teacherRepository.save(teacher);
        return mapToResponse(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    private TeacherResponse mapToResponse(Teacher teacher) {
        TeacherResponse response = new TeacherResponse();
        response.setId(teacher.getId());
        response.setFullName(teacher.getUser().getFullName());
        response.setEmail(teacher.getUser().getEmail());
        response.setDepartmentName(teacher.getDepartment().getName());
        if (teacher.getAdvisorClass() != null) {
            response.setAdvisorClassName(teacher.getAdvisorClass().getName());
        }
        response.setSubjectsTaught(teacher.getSubjects().stream()
                .map(subject -> subject.getName()).collect(Collectors.toList()));
        return response;
    }
}
