package com.attendance.backend.service;
<<<<<<< HEAD
public class StudentService {
    
=======

import com.attendance.backend.entity.Student;
import com.attendance.backend.exception.ResourceNotFoundException;
import com.attendance.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student addStudent(Student student) {
        student.setCreatedAt(LocalDateTime.now());
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with roll number: " + rollNumber));
    }

    public Student updateStudent(String id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        student.setName(studentDetails.getName());
        student.setRollNumber(studentDetails.getRollNumber());
        student.setParentDetails(studentDetails.getParentDetails());
        student.setAddress(studentDetails.getAddress());
        student.setMobileNumber(studentDetails.getMobileNumber());

        return studentRepository.save(student);
    }

    public void deleteStudent(String id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found");
        }
        studentRepository.deleteById(id);
    }
>>>>>>> a39a58e7ca679a641a1cf6d2cece10124c75ab61
}
