package com.example.sms.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ClassPerformanceResponse {
    private Long classId;
    private String className;
    private List<SubjectPerformance> subjectPerformances;

    @Data
    public static class SubjectPerformance {
        private String subjectName;
        private Double averagePercentage;
        private Integer passCount;
        private Integer failCount;
    }
}
