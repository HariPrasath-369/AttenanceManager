package com.example.sms.dto.response;

import lombok.Data;

@Data
public class StudentAttendanceSummaryResponse {
    private Long studentId;
    private Integer totalDays; // A day might mean a session or actual days. User prompt: "totalDays": 50
    private Integer presentDays; // User prompt: "presentDays": 42
    private Double percentage; // User prompt: "percentage": 84.0
}
