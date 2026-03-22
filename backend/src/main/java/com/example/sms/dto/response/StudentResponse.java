package com.example.sms.dto.response;

import lombok.Data;

@Data
public class StudentResponse {
    private Long id;
    private String fullName;
    private String email;
    private String rollNumber;
    private String className;
    private String departmentName;
    private String dateOfBirth;
    private String parentContact;
    private String parentEmail;
}
