package com.example.sms.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class AttendanceResponse {
    private Long id;
    private LocalDate date;
    private String session;
    private Boolean isLocked;
    private List<StudentAttendance> studentAttendances;

    @Data
    public static class StudentAttendance {
        private Long studentId;
        private String studentName;
        private String status;
    }
}
