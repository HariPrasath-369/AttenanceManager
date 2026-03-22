package com.example.sms.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class MarksResponse {
    private Long sheetId;
    private String sheetType;
    private String status; // DRAFT, SUBMITTED, LOCKED
    private List<StudentMarks> marks;

    @Data
    public static class StudentMarks {
        private Long studentId;
        private String studentName;
        private Double assessmentMarks;
        private Double practicalMarks;
        private Double semesterMarks;
        private Double marksObtained;
        private Double maxMarks;
        private String grade;
    }
}
