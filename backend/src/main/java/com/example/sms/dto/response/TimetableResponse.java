package com.example.sms.dto.response;

import lombok.Data;

@Data
public class TimetableResponse {
    private Long id;
    private Integer dayOfWeek;
    private Integer period;
    private Long classId;
    private String className;
    private String subjectCode;
    private String subjectName;
    private String teacherName;
    private String venue;
}
