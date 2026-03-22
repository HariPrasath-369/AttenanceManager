package com.example.sms.scheduler;

import com.example.sms.entity.Student;
import com.example.sms.repository.StudentRepository;
import com.example.sms.service.AttendanceService;
import com.example.sms.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class AlertScheduler {
    private final StudentRepository studentRepository;
    private final AttendanceService attendanceService;
    private final NotificationService notificationService;

    public AlertScheduler(StudentRepository studentRepository,
                          AttendanceService attendanceService,
                          NotificationService notificationService) {
        this.studentRepository = studentRepository;
        this.attendanceService = attendanceService;
        this.notificationService = notificationService;
    }

    // Run daily at 8 PM
    @Scheduled(cron = "0 0 20 * * ?")
    public void checkLowAttendance() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30); // Last 30 days
        List<Student> allStudents = studentRepository.findAll();
        for (Student student : allStudents) {
            double percentage = attendanceService.getStudentAttendanceSummary(student.getId(), startDate, endDate).getPercentage();
            if (percentage < 75.0) {
                String message = String.format("Your attendance is below 75%% (%.2f%%). Please maintain regular attendance.", percentage);
                notificationService.sendNotification(student.getUser().getId(), "Low Attendance Alert", message);
                // Also notify parent if needed
            }
        }
    }
}
