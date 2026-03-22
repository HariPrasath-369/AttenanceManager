package com.example.sms.service;

import com.example.sms.dto.response.DashboardResponse;
import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;

    public AnalyticsService(StudentRepository studentRepository,
                            DepartmentRepository departmentRepository,
                            ClassRepository classRepository,
                            TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.classRepository = classRepository;
        this.teacherRepository = teacherRepository;
    }

    public DashboardResponse getPrincipalDashboard() {
        DashboardResponse response = new DashboardResponse();
        response.setRole("PRINCIPAL");
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDepartments", departmentRepository.count());
        stats.put("totalClasses", classRepository.count());
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalTeachers", teacherRepository.count());
        
        // Teachers per department
        Map<String, Long> teachersByDept = departmentRepository.findAll().stream()
                .filter(dept -> dept.getName() != null)
                .collect(Collectors.toMap(
                        Department::getName,
                        dept -> teacherRepository.countByDepartmentId(dept.getId())
                ));
        stats.put("teachersByDepartment", (Object)teachersByDept);
        
        response.setStatistics(stats);
        return response;
    }

    public DashboardResponse getHodDashboard(Long hodId) {
        Department dept = departmentRepository.findByHods_Id(hodId)
                .orElseThrow(() -> new RuntimeException("Department not found for HOD"));
        DashboardResponse response = new DashboardResponse();
        response.setRole("HOD");
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalTeachers", teacherRepository.countByDepartmentId(dept.getId()));
        stats.put("totalClasses", classRepository.findByDepartmentId(dept.getId()).size());
        stats.put("totalStudents", studentRepository.countByClassEntity_Department_Id(dept.getId()));
        response.setStatistics(stats);
        return response;
    }

    public DashboardResponse getClassAdvisorDashboard(Long teacherId) {
        Teacher advisor = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        ClassEntity ce = advisor.getAdvisorClass();
        if (ce == null) {
            throw new RuntimeException("Teacher is not a Class Advisor");
        }
        
        DashboardResponse response = new DashboardResponse();
        response.setRole("CLASS_ADVISOR");
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalStudents", ce.getStudents().size());
        stats.put("className", ce.getName());
        response.setStatistics(stats);
        return response;
    }
}
