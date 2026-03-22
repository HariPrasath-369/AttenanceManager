package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // If a teacher is a Class Advisor for a class
    @OneToOne(mappedBy = "classAdvisor")
    private ClassEntity advisorClass;

    // Subjects taught by this teacher
    @ManyToMany
    @JoinTable(
        name = "teacher_subjects",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects = new ArrayList<>();

    // Classes where this teacher is subject teacher (through ClassSubject)
    @OneToMany(mappedBy = "teacher")
    private List<ClassSubject> classSubjects = new ArrayList<>();

    @Column(name = "joining_date")
    private String joiningDate;
}
