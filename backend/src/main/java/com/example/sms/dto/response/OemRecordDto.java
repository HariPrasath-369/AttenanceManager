package com.example.sms.dto.response;

import lombok.Data;

@Data
public class OemRecordDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private String registerNumber;
    private Double assessmentScore;
    private Double practicalScore;
    private Double semesterScore;
    private Double totalScore;
    private String grade;
}
