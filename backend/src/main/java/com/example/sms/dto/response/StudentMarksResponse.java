package com.example.sms.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class StudentMarksResponse {
    private Long studentId;
    private String studentName;
    private String registerNumber;
    private List<SubjectMark> marks;

    @Data
    public static class SubjectMark {
        private String subjectName;
        private Integer semester;
        private Double totalScore;
        private String grade;
        private String status;
    }
}
