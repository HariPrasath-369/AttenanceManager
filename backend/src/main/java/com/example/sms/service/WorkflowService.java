package com.example.sms.service;

import com.example.sms.dto.request.RequestDto;
import com.example.sms.dto.response.RequestResponse;
import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkflowService {
    private final RequestRepository requestRepository;
    private final StudentRepository studentRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public WorkflowService(RequestRepository requestRepository,
                           StudentRepository studentRepository,
                           NotificationService notificationService,
                           AuditService auditService) {
        this.requestRepository = requestRepository;
        this.studentRepository = studentRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    @Transactional
    public RequestResponse createLeaveOrOdRequest(RequestDto requestDto, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Teacher classAdvisor = student.getClassEntity().getClassAdvisor();
        if (classAdvisor == null) {
            throw new RuntimeException("No class advisor assigned for this class");
        }
        Request request = new Request();
        request.setStudent(student);
        request.setTeacher(classAdvisor);
        request.setType(Request.RequestType.valueOf(requestDto.getType()));
        request.setDescription(requestDto.getDescription());
        request.setStatus(Request.Status.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request = requestRepository.save(request);
        notificationService.sendNotification(classAdvisor.getUser().getId(),
                "New request from " + student.getUser().getFullName(),
                "Type: " + request.getType() + ", Description: " + request.getDescription());
        auditService.log("CREATE_REQUEST", "Request", request.getId(), null, request.getStatus().name());
        return mapToResponse(request);
    }

    @Transactional
    public RequestResponse approveRequest(Long requestId, Long teacherId, String comments) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (!request.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("You are not authorized to approve this request");
        }
        if (request.getStatus() != Request.Status.PENDING) {
            throw new RuntimeException("Request already processed");
        }
        String oldStatus = request.getStatus().name();
        request.setStatus(Request.Status.APPROVED);
        request.setComments(comments);
        request.setUpdatedAt(LocalDateTime.now());
        request = requestRepository.save(request);
        notificationService.sendNotification(request.getStudent().getUser().getId(),
                "Request approved", "Your " + request.getType() + " request has been approved.");
        auditService.log("APPROVE_REQUEST", "Request", requestId, oldStatus, "APPROVED");
        return mapToResponse(request);
    }

    @Transactional
    public RequestResponse rejectRequest(Long requestId, Long teacherId, String comments) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (!request.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("You are not authorized to reject this request");
        }
        if (request.getStatus() != Request.Status.PENDING) {
            throw new RuntimeException("Request already processed");
        }
        String oldStatus = request.getStatus().name();
        request.setStatus(Request.Status.REJECTED);
        request.setComments(comments);
        request.setUpdatedAt(LocalDateTime.now());
        request = requestRepository.save(request);
        notificationService.sendNotification(request.getStudent().getUser().getId(),
                "Request rejected", "Your " + request.getType() + " request has been rejected. Reason: " + comments);
        auditService.log("REJECT_REQUEST", "Request", requestId, oldStatus, "REJECTED");
        return mapToResponse(request);
    }

    private RequestResponse mapToResponse(Request request) {
        RequestResponse response = new RequestResponse();
        response.setId(request.getId());
        response.setType(request.getType().name());
        response.setDescription(request.getDescription());
        response.setStatus(request.getStatus().name());
        response.setStudentName(request.getStudent().getUser().getFullName());
        response.setStudentRollNumber(request.getStudent().getRollNumber());
        response.setCreatedAt(request.getCreatedAt());
        response.setComments(request.getComments());
        return response;
    }

    public List<RequestResponse> getPendingSemesterRequestsForDepartment(Long deptId) {
        return requestRepository.findByClassIdAndTypeAndStatus(deptId, Request.RequestType.SEMESTER_START, Request.Status.PENDING).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveSemesterStart(Long requestId, Long hodId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        String oldStatus = request.getStatus().name();
        request.setStatus(Request.Status.APPROVED);
        request.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(request);
        auditService.log("APPROVE_SEMESTER_START", "Request", requestId, oldStatus, "APPROVED");
    }
    @Transactional(readOnly = true)
    public List<RequestResponse> getRequestsByStudent(Long studentId) {
        return requestRepository.findAll().stream()
                .filter(r -> r.getStudent().getId().equals(studentId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequestResponse> getRequestsByTeacher(Long teacherId) {
        return requestRepository.findAll().stream()
                .filter(r -> r.getTeacher() != null && r.getTeacher().getId().equals(teacherId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
