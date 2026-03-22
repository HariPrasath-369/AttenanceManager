package com.example.sms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class TimetableRequest {
    @NotNull
    private Long classId;

    @NotNull
    private Integer dayOfWeek;

    @NotNull
    private Integer period;

    @NotNull
    private Long subjectId;

    @NotNull
    private Long teacherId;

    private String venue;
}
