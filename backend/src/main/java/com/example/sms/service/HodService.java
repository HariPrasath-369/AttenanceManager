package com.example.sms.service;

import com.example.sms.dto.request.HodRequest;
import com.example.sms.dto.response.HodResponse;
import com.example.sms.entity.Department;
import com.example.sms.entity.Hod;
import com.example.sms.entity.User;
import com.example.sms.repository.DepartmentRepository;
import com.example.sms.repository.HodRepository;
import com.example.sms.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HodService {
    private final HodRepository hodRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public HodService(HodRepository hodRepository,
                      UserRepository userRepository,
                      DepartmentRepository departmentRepository) {
        this.hodRepository = hodRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public HodResponse createHod(HodRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        if (hodRepository.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException("User is already assigned as HOD");
        }
        Hod hod = new Hod();
        hod.setUser(user);
        hod.setDepartment(dept);
        hod.setJoiningDate(request.getJoiningDate().toString());
        hod = hodRepository.save(hod);
        return mapToResponse(hod);
    }

    @Transactional(readOnly = true)
    public List<HodResponse> getHodsByDepartment(Long deptId) {
        return hodRepository.findByDepartmentId(deptId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HodResponse getHodById(Long id) {
        Hod hod = hodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HOD not found"));
        return mapToResponse(hod);
    }

    @Transactional
    public HodResponse updateHod(Long id, HodRequest request) {
        Hod hod = hodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HOD not found"));
        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            hod.setDepartment(dept);
        }
        hod.setJoiningDate(request.getJoiningDate().toString());
        hod = hodRepository.save(hod);
        return mapToResponse(hod);
    }

    @Transactional
    public void deleteHod(Long id) {
        hodRepository.deleteById(id);
    }

    public List<HodResponse> getAllHods() {
        return hodRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private HodResponse mapToResponse(Hod hod) {
        HodResponse response = new HodResponse();
        response.setId(hod.getId());
        response.setUserId(hod.getUser().getId());
        response.setUserName(hod.getUser().getFullName());
        response.setDepartmentId(hod.getDepartment().getId());
        response.setDepartmentName(hod.getDepartment().getName());
        response.setJoiningDate(hod.getJoiningDate() != null ? LocalDate.parse(hod.getJoiningDate()) : null);
        return response;
    }
}
