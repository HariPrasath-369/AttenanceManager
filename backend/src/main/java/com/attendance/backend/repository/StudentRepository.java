package com.attendance.backend.repository;

<<<<<<< HEAD
public class StudentRepository {
    
=======
import com.attendance.backend.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByRollNumber(String rollNumber);
>>>>>>> a39a58e7ca679a641a1cf6d2cece10124c75ab61
}
