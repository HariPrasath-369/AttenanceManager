package com.example.sms.dto.response;

import lombok.Data;
import java.util.Map;

@Data
public class DashboardResponse {
    private String role;
    private Map<String, Object> statistics;
    private Object chartData;
}
