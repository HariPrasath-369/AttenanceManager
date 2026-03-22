package com.example.sms.service;

import com.example.sms.dto.request.OemMarksUpdateRequest;
import com.example.sms.dto.request.OemSheetCreateRequest;
import com.example.sms.dto.response.OemRecordDto;
import com.example.sms.dto.response.OemSheetDto;
import com.example.sms.dto.response.StudentMarksResponse;
import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OemService {
    private final OemSheetRepository oemSheetRepository;
    private final OemRecordRepository oemRecordRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final AuditService auditService;

    public OemService(OemSheetRepository oemSheetRepository,
                      OemRecordRepository oemRecordRepository,
                      ClassRepository classRepository,
                      SubjectRepository subjectRepository,
                      TeacherRepository teacherRepository,
                      StudentRepository studentRepository,
                      AuditService auditService) {
        this.oemSheetRepository = oemSheetRepository;
        this.oemRecordRepository = oemRecordRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.auditService = auditService;
    }

    @Transactional
    public OemSheetDto createSheet(OemSheetCreateRequest request) {
        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (oemSheetRepository.findByClassEntityIdAndSubjectIdAndSheetTypeAndStatusNot(
                classEntity.getId(), subject.getId(), request.getSheetType(), OemSheet.Status.LOCKED).isPresent()) {
            throw new RuntimeException("An active sheet for this subject already exists");
        }

        OemSheet sheet = new OemSheet();
        sheet.setClassEntity(classEntity);
        sheet.setSubject(subject);
        sheet.setTeacher(teacher);
        sheet.setSemester(request.getSemester());
        sheet.setSheetType(request.getSheetType());
        sheet.setMaxMarks(request.getMaxMarks());
        sheet.setStatus(OemSheet.Status.DRAFT);
        sheet.setCreatedAt(LocalDateTime.now());
        sheet.setUpdatedAt(LocalDateTime.now());

        List<Student> students = studentRepository.findByClassEntityId(classEntity.getId());
        List<OemRecord> records = new ArrayList<>();

        for (Student student : students) {
            OemRecord record = new OemRecord();
            record.setSheet(sheet);
            record.setStudent(student);
            record.setAssessmentScore(0.0);
            record.setPracticalScore(0.0);
            record.setSemesterScore(0.0);
            record.setTotalScore(0.0);
            record.setGrade("NA");
            records.add(record);
        }
        sheet.setRecords(records);
        oemSheetRepository.save(sheet);

        auditService.log("CREATE_SHEET", "OemSheet", sheet.getId(), "NULL", "DRAFT");
        return mapToDto(sheet);
    }

    @Transactional
    public OemSheetDto updateMarks(OemMarksUpdateRequest request) {
        OemSheet sheet = oemSheetRepository.findByIdWithRecords(request.getSheetId())
                .orElseThrow(() -> new RuntimeException("Sheet not found"));

        if (sheet.getStatus() != OemSheet.Status.DRAFT) {
            throw new RuntimeException("Cannot edit marks for a sheet that is not in DRAFT status");
        }

        for (OemMarksUpdateRequest.StudentMarkDto sm : request.getMarks()) {
            OemRecord record = sheet.getRecords().stream()
                    .filter(r -> r.getStudent().getId().equals(sm.getStudentId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Student record not found in this sheet: " + sm.getStudentId()));

            record.setAssessmentScore(sm.getAssessmentScore());
            record.setPracticalScore(sm.getPracticalScore());
            record.setSemesterScore(sm.getSemesterScore());

            Double total = sm.getAssessmentScore() + sm.getPracticalScore() + sm.getSemesterScore();
            record.setTotalScore(total);
            record.setGrade(calculateGrade(total));
            record.setUpdatedAt(LocalDateTime.now());
        }

        sheet.setUpdatedAt(LocalDateTime.now());
        oemSheetRepository.save(sheet);
        return mapToDto(sheet);
    }

    @Transactional
    public void submitSheet(Long sheetId) {
        OemSheet sheet = oemSheetRepository.findById(sheetId)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));
        if (sheet.getStatus() == OemSheet.Status.DRAFT) {
            sheet.setStatus(OemSheet.Status.SUBMITTED);
            sheet.setUpdatedAt(LocalDateTime.now());
            oemSheetRepository.save(sheet);
            auditService.log("SUBMIT_SHEET", "OemSheet", sheetId, "DRAFT", "SUBMITTED");
        } else {
            throw new RuntimeException("Sheet must be in DRAFT status to submit");
        }
    }

    @Transactional
    public void lockSheet(Long sheetId) {
        OemSheet sheet = oemSheetRepository.findById(sheetId)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));
        if (sheet.getStatus() == OemSheet.Status.SUBMITTED) {
            sheet.setStatus(OemSheet.Status.LOCKED);
            sheet.setUpdatedAt(LocalDateTime.now());
            oemSheetRepository.save(sheet);
            auditService.log("LOCK_SHEET", "OemSheet", sheetId, "SUBMITTED", "LOCKED");
        } else {
            throw new RuntimeException("Sheet must be in SUBMITTED status to lock");
        }
    }

    public List<OemSheetDto> getSheetsByClass(Long classId) {
        return oemSheetRepository.findByClassId(classId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public StudentMarksResponse getStudentMarks(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<OemRecord> records = oemRecordRepository.findByStudentId(studentId);
        
        StudentMarksResponse response = new StudentMarksResponse();
        response.setStudentId(student.getId());
        response.setStudentName(student.getUser().getFullName());
        response.setRegisterNumber(student.getRollNumber());

        List<StudentMarksResponse.SubjectMark> subjectMarks = new ArrayList<>();
        for (OemRecord record : records) {
            StudentMarksResponse.SubjectMark sm = new StudentMarksResponse.SubjectMark();
            sm.setSubjectName(record.getSheet().getSubject().getName());
            sm.setSemester(record.getSheet().getSemester());
            // Only show marks if sheet is locked
            if (record.getSheet().getStatus() == OemSheet.Status.LOCKED) {
                sm.setTotalScore(record.getTotalScore());
                sm.setGrade(record.getGrade());
            } else {
                sm.setGrade("Pending");
            }
            sm.setStatus(record.getSheet().getStatus().name());
            subjectMarks.add(sm);
        }
        response.setMarks(subjectMarks);
        return response;
    }

    private String calculateGrade(Double total) {
        if (total == null) return "NA";
        if (total >= 90) return "O";
        if (total >= 80) return "A+";
        if (total >= 70) return "A";
        if (total >= 60) return "B+";
        if (total >= 50) return "B";
        return "RA";
    }

    private OemSheetDto mapToDto(OemSheet sheet) {
        OemSheetDto dto = new OemSheetDto();
        dto.setId(sheet.getId());
        dto.setClassId(sheet.getClassEntity().getId());
        dto.setClassName(sheet.getClassEntity().getName());
        dto.setSubjectId(sheet.getSubject().getId());
        dto.setSubjectName(sheet.getSubject().getName());
        dto.setTeacherId(sheet.getTeacher().getId());
        dto.setTeacherName(sheet.getTeacher().getUser().getFullName());
        dto.setSemester(sheet.getSemester());
        dto.setSheetType(sheet.getSheetType());
        dto.setMaxMarks(sheet.getMaxMarks());
        dto.setStatus(sheet.getStatus().name());
        dto.setCreatedAt(sheet.getCreatedAt());
        dto.setUpdatedAt(sheet.getUpdatedAt());

        if (sheet.getRecords() != null) {
            List<OemRecordDto> recordDtos = sheet.getRecords().stream().map(r -> {
                OemRecordDto rd = new OemRecordDto();
                rd.setId(r.getId());
                rd.setStudentId(r.getStudent().getId());
                rd.setStudentName(r.getStudent().getUser().getFullName());
                rd.setRegisterNumber(r.getStudent().getRollNumber());
                rd.setAssessmentScore(r.getAssessmentScore());
                rd.setPracticalScore(r.getPracticalScore());
                rd.setSemesterScore(r.getSemesterScore());
                rd.setTotalScore(r.getTotalScore());
                rd.setGrade(r.getGrade());
                return rd;
            }).collect(Collectors.toList());
            dto.setRecords(recordDtos);
        }
        return dto;
    }
}
