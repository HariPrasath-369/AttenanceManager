package com.example.sms.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ClassResponse {
    private Long id;
    private String name;
    private String departmentName;
    private Integer year;
    private Integer semester;
    private Integer capacity;
    private String classAdvisorName;
    private Integer studentCount;
    private List<SubjectTeacherResponse> subjects;

    @Data
    public static class SubjectTeacherResponse {
        private String subjectName;
        private String teacherName;
    }
}
