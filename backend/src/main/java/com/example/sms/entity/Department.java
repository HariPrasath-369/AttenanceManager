package com.example.sms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String code;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Hod> hods = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    private List<ClassEntity> classes = new ArrayList<>();

    public Hod getHod() {
        if (hods == null || hods.isEmpty()) return null;
        return hods.get(0); // For now, assume one HOD per department
    }
}
