package com.example.sms.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OemSheetDto {
    private Long id;
    private Long classId;
    private String className;
    private Long subjectId;
    private String subjectName;
    private Long teacherId;
    private String teacherName;
    private Integer semester;
    private String sheetType;
    private Integer maxMarks;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OemRecordDto> records;
}
