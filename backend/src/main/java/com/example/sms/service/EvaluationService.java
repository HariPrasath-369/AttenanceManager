package com.example.sms.service;

import com.example.sms.dto.response.ClassPerformanceResponse;
import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationService {
    private final OemRecordRepository oemRecordRepository;
    private final ClassSubjectRepository classSubjectRepository;
    private final ClassRepository classRepository;
    private final UserContextService userContextService;

    public EvaluationService(OemSheetRepository oemSheetRepository,
            OemRecordRepository oemRecordRepository,
            ClassSubjectRepository classSubjectRepository,
            ClassRepository classRepository,
            UserContextService userContextService) {
        this.oemRecordRepository = oemRecordRepository;
        this.classSubjectRepository = classSubjectRepository;
        this.classRepository = classRepository;
        this.userContextService = userContextService;
    }

    @Transactional(readOnly = true)
    public List<ClassPerformanceResponse> getPerformanceForCurrentHod() {
        Hod hod = userContextService.getCurrentHod();
        return getPerformanceByDepartment(hod.getDepartment().getId());
    }

    @Transactional(readOnly = true)
    public ClassPerformanceResponse getPerformanceForCurrentAdvisor() {
        Teacher advisor = userContextService.getCurrentTeacher();
        ClassEntity classEntity = advisor.getAdvisorClass();
        if (classEntity == null) {
            throw new RuntimeException("You are not assigned as a Class Advisor");
        }

        ClassPerformanceResponse cpr = new ClassPerformanceResponse();
        cpr.setClassId(classEntity.getId());
        cpr.setClassName(classEntity.getName());

        List<ClassPerformanceResponse.SubjectPerformance> subjectPerformances = new ArrayList<>();
        List<ClassSubject> classSubjects = classSubjectRepository.findByClassEntityId(classEntity.getId());

        for (ClassSubject cs : classSubjects) {
            ClassPerformanceResponse.SubjectPerformance sp = new ClassPerformanceResponse.SubjectPerformance();
            sp.setSubjectName(cs.getSubject().getName());

            List<OemRecord> records = oemRecordRepository
                    .findBySheet_ClassEntity_IdAndSheet_Subject_Id(classEntity.getId(), cs.getSubject().getId());
            if (!records.isEmpty()) {
                double totalPercentage = 0;
                int passCount = 0;
                for (OemRecord record : records) {
                    double percentage = (record.getTotalScore() / record.getSheet().getMaxMarks()) * 100;
                    totalPercentage += percentage;
                    if (percentage >= 40)
                        passCount++;
                }
                sp.setAveragePercentage(totalPercentage / records.size());
                sp.setPassCount(passCount);
                sp.setFailCount(records.size() - passCount);
            } else {
                sp.setAveragePercentage(0.0);
                sp.setPassCount(0);
                sp.setFailCount(0);
            }
            subjectPerformances.add(sp);
        }
        cpr.setSubjectPerformances(subjectPerformances);
        return cpr;
    }

    @Transactional(readOnly = true)
    public List<ClassPerformanceResponse> getPerformanceByDepartment(Long deptId) {
        List<ClassEntity> classes = classRepository.findByDepartmentId(deptId);
        List<ClassPerformanceResponse> performanceList = new ArrayList<>();

        for (ClassEntity classEntity : classes) {
            ClassPerformanceResponse cpr = new ClassPerformanceResponse();
            cpr.setClassId(classEntity.getId());
            cpr.setClassName(classEntity.getName());

            List<ClassPerformanceResponse.SubjectPerformance> subjectPerformances = new ArrayList<>();
            List<ClassSubject> classSubjects = classSubjectRepository.findByClassEntityId(classEntity.getId());

            for (ClassSubject cs : classSubjects) {
                ClassPerformanceResponse.SubjectPerformance sp = new ClassPerformanceResponse.SubjectPerformance();
                sp.setSubjectName(cs.getSubject().getName());

                List<OemRecord> records = oemRecordRepository
                        .findBySheet_ClassEntity_IdAndSheet_Subject_Id(classEntity.getId(), cs.getSubject().getId());
                if (!records.isEmpty()) {
                    double totalPercentage = 0;
                    int passCount = 0;
                    for (OemRecord record : records) {
                        double percentage = (record.getTotalScore() / record.getSheet().getMaxMarks()) * 100;
                        totalPercentage += percentage;
                        if (percentage >= 40)
                            passCount++;
                    }
                    sp.setAveragePercentage(totalPercentage / records.size());
                    sp.setPassCount(passCount);
                    sp.setFailCount(records.size() - passCount);
                } else {
                    sp.setAveragePercentage(0.0);
                    sp.setPassCount(0);
                    sp.setFailCount(0);
                }
                subjectPerformances.add(sp);
            }
            cpr.setSubjectPerformances(subjectPerformances);
            performanceList.add(cpr);
        }
        return performanceList;
    }
}
