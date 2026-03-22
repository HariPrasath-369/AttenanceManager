package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., "CS-A"

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private Integer year;
    private Integer semester;
    private Integer capacity;

    @OneToOne
    @JoinColumn(name = "class_advisor_id")
    private Teacher classAdvisor;

    @OneToMany(mappedBy = "classEntity")
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "classEntity")
    private List<ClassSubject> classSubjects = new ArrayList<>();

    @OneToMany(mappedBy = "classEntity")
    private List<Timetable> timetables = new ArrayList<>();

    @OneToMany(mappedBy = "classEntity")
    private List<Attendance> attendances = new ArrayList<>();
}
