package com.example.sms.dto.response;

import lombok.Data;

@Data
public class ClassSubjectResponse {
    private Long id;
    private String className;
    private String subjectName;
    private String teacherName;
}
