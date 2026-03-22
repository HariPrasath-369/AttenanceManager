package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class AttendanceRequest {
    @NotNull
    private Long classId;

    @NotNull
    private LocalDate date;

    @NotNull
    private String session; // MORNING, AFTERNOON

    @NotEmpty
    private List<AttendanceRecordDto> records;

    @Data
    public static class AttendanceRecordDto {
        @NotNull
        private Long studentId;
        
        @NotNull
        private String status; // PRESENT, ABSENT, OD, LEAVE
    }
}
