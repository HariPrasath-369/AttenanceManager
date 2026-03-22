package com.example.sms.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class HodRequest {
    private Long userId;
    private Long departmentId;
    private LocalDate joiningDate;
}
