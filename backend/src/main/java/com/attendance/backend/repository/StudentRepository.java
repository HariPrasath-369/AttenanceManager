package com.attendance.backend.repository;

<<<<<<< HEAD
=======
<<<<<<< HEAD
public class StudentRepository {
    
=======
>>>>>>> e129198e3070a4eeb3e8a3fe91bd4b99ac2df0c6
import com.attendance.backend.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByRollNumber(String rollNumber);
<<<<<<< HEAD
=======
>>>>>>> a39a58e7ca679a641a1cf6d2cece10124c75ab61
>>>>>>> e129198e3070a4eeb3e8a3fe91bd4b99ac2df0c6
}
