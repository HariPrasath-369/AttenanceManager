package com.example.sms.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SemesterResponse {
    private Long id;
    private String className;
    private Integer year;
    private Integer semesterNumber;
    private String status; // PENDING, ACTIVE, ENDED
    private LocalDate startDate;
    private LocalDate endDate;
}
