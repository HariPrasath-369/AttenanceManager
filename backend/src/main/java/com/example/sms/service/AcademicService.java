package com.example.sms.service;

import com.example.sms.dto.request.*;
import com.example.sms.dto.response.*;
import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcademicService {
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final ClassSubjectRepository classSubjectRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final UserContextService userContextService;

    public AcademicService(ClassRepository classRepository,
                           SubjectRepository subjectRepository,
                           ClassSubjectRepository classSubjectRepository,
                           DepartmentRepository departmentRepository,
                           TeacherRepository teacherRepository,
                           UserContextService userContextService) {
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.classSubjectRepository = classSubjectRepository;
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
        this.userContextService = userContextService;
    }

    @Transactional
    public ClassResponse createClass(ClassRequest request) {
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        Teacher advisor = null;
        if (request.getClassAdvisorId() != null) {
            advisor = teacherRepository.findById(request.getClassAdvisorId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
        }
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName(request.getName());
        classEntity.setDepartment(dept);
        classEntity.setYear(request.getYear());
        classEntity.setSemester(request.getSemester());
        classEntity.setCapacity(request.getCapacity());
        classEntity.setClassAdvisor(advisor);
        classEntity = classRepository.save(classEntity);
        return mapClassToResponse(classEntity);
    }

    @Transactional
    public SubjectResponse createSubject(SubjectRequest request) {
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setCode(request.getCode());
        subject.setDepartment(dept);
        subject = subjectRepository.save(subject);
        SubjectResponse response = new SubjectResponse();
        response.setId(subject.getId());
        response.setName(subject.getName());
        response.setCode(subject.getCode());
        response.setDepartmentName(dept.getName());
        return response;
    }

    @Transactional
    public ClassSubjectResponse assignTeacherToSubject(ClassSubjectRequest request) {
        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        if (classSubjectRepository.findByClassEntityIdAndSubjectId(request.getClassId(), request.getSubjectId()).isPresent()) {
            throw new RuntimeException("Subject already assigned to this class");
        }
        ClassSubject cs = new ClassSubject();
        cs.setClassEntity(classEntity);
        cs.setSubject(subject);
        cs.setTeacher(teacher);
        cs = classSubjectRepository.save(cs);
        ClassSubjectResponse response = new ClassSubjectResponse();
        response.setId(cs.getId());
        response.setClassName(classEntity.getName());
        response.setSubjectName(subject.getName());
        response.setTeacherName(teacher.getUser().getFullName());
        return response;
    }

    @Transactional
    public ClassResponse assignClassAdvisor(Long classId, Long teacherId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        classEntity.setClassAdvisor(teacher);
        classEntity = classRepository.save(classEntity);
        return mapClassToResponse(classEntity);
    }

    @Transactional(readOnly = true)
    public List<ClassResponse> getClassesForCurrentHod() {
        Long deptId = userContextService.getCurrentHod().getDepartment().getId();
        return classRepository.findByDepartmentId(deptId).stream()
                .map(this::mapClassToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> getSubjectsForCurrentHod() {
        Long deptId = userContextService.getCurrentHod().getDepartment().getId();
        return subjectRepository.findByDepartmentId(deptId).stream()
                .map(sub -> {
                    SubjectResponse resp = new SubjectResponse();
                    resp.setId(sub.getId());
                    resp.setName(sub.getName());
                    resp.setCode(sub.getCode());
                    resp.setDepartmentName(sub.getDepartment().getName());
                    return resp;
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClassResponse> getClassesByDepartment(Long deptId) {
        return classRepository.findByDepartmentId(deptId).stream()
                .map(this::mapClassToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> getSubjectsByDepartment(Long deptId) {
        return subjectRepository.findByDepartmentId(deptId).stream()
                .map(sub -> {
                    SubjectResponse resp = new SubjectResponse();
                    resp.setId(sub.getId());
                    resp.setName(sub.getName());
                    resp.setCode(sub.getCode());
                    resp.setDepartmentName(sub.getDepartment().getName());
                    return resp;
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClassResponse> getClassesForCurrentTeacher() {
        Teacher teacher = userContextService.getCurrentTeacher();
        return classSubjectRepository.findAll().stream()
                .filter(cs -> cs.getTeacher().getId().equals(teacher.getId()))
                .map(cs -> mapClassToResponse(cs.getClassEntity()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> getSubjectsForCurrentTeacher() {
        Teacher teacher = userContextService.getCurrentTeacher();
        return classSubjectRepository.findAll().stream()
                .filter(cs -> cs.getTeacher().getId().equals(teacher.getId()))
                .map(cs -> mapSubjectToResponse(cs.getSubject()))
                .distinct()
                .collect(Collectors.toList());
    }

    private SubjectResponse mapSubjectToResponse(Subject sub) {
        SubjectResponse resp = new SubjectResponse();
        resp.setId(sub.getId());
        resp.setName(sub.getName());
        resp.setCode(sub.getCode());
        resp.setDepartmentName(sub.getDepartment().getName());
        return resp;
    }

    private ClassResponse mapClassToResponse(ClassEntity classEntity) {
        ClassResponse response = new ClassResponse();
        response.setId(classEntity.getId());
        response.setName(classEntity.getName());
        response.setDepartmentName(classEntity.getDepartment().getName());
        response.setYear(classEntity.getYear());
        response.setSemester(classEntity.getSemester());
        response.setCapacity(classEntity.getCapacity());
        if (classEntity.getClassAdvisor() != null) {
            response.setClassAdvisorName(classEntity.getClassAdvisor().getUser().getFullName());
        }
        response.setStudentCount(classEntity.getStudents() != null ? classEntity.getStudents().size() : 0);
        List<ClassSubject> csList = classSubjectRepository.findByClassEntityId(classEntity.getId());
        response.setSubjects(csList.stream()
                .map(cs -> {
                    ClassResponse.SubjectTeacherResponse st = new ClassResponse.SubjectTeacherResponse();
                    st.setSubjectName(cs.getSubject().getName());
                    st.setTeacherName(cs.getTeacher().getUser().getFullName());
                    return st;
                }).collect(Collectors.toList()));
        return response;
    }
}
