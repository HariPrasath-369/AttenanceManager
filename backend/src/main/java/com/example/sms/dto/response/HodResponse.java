package com.example.sms.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class HodResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long departmentId;
    private String departmentName;
    private LocalDate joiningDate;
}
