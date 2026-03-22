package com.example.sms.controller;

import com.example.sms.dto.response.DashboardResponse;
import com.example.sms.entity.Role;
import com.example.sms.entity.User;
import com.example.sms.service.AnalyticsService;
import com.example.sms.service.UserContextService;
import org.springframework.http.ResponseEntity;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final UserContextService userContextService;

    public AnalyticsController(AnalyticsService analyticsService, UserContextService userContextService) {
        this.analyticsService = analyticsService;
        this.userContextService = userContextService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {
        User user = userContextService.getCurrentUser();
        Set<Role> roles = user.getRoles();
        
        boolean isPrincipal = roles.stream().anyMatch(r -> r.getName().equals("PRINCIPAL"));
        boolean isHod = roles.stream().anyMatch(r -> r.getName().equals("HOD"));
        boolean isAdvisor = roles.stream().anyMatch(r -> r.getName().equals("CLASS_ADVISOR"));
        
        if (isPrincipal) {
            return ResponseEntity.ok(analyticsService.getPrincipalDashboard());
        } else if (isHod) {
            Long hodId = userContextService.getCurrentHod().getId();
            return ResponseEntity.ok(analyticsService.getHodDashboard(hodId));
        } else if (isAdvisor) {
            Long teacherId = userContextService.getCurrentTeacher().getId();
            return ResponseEntity.ok(analyticsService.getClassAdvisorDashboard(teacherId));
        }
        
        DashboardResponse empty = new DashboardResponse();
        empty.setRole(roles.isEmpty() ? "UNKNOWN" : roles.iterator().next().getName());
        return ResponseEntity.ok(empty);
    }
}
