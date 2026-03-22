package com.example.sms.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class TeacherResponse {
    private Long id;
    private String fullName;
    private String email;
    private String departmentName;
    private String advisorClassName;
    private List<String> subjectsTaught;
}
