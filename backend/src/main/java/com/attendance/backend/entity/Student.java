package com.attendance.backend.entity;

<<<<<<< HEAD
=======
<<<<<<< HEAD
public class Student {
    
=======
>>>>>>> e129198e3070a4eeb3e8a3fe91bd4b99ac2df0c6
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "students")
public class Student {
    @Id
    private String id;
    private String name;
    private String rollNumber;
    private LocalDateTime createdAt;
    private String parentDetails;
    private String address;
    private String mobileNumber;
<<<<<<< HEAD
=======
>>>>>>> a39a58e7ca679a641a1cf6d2cece10124c75ab61
>>>>>>> e129198e3070a4eeb3e8a3fe91bd4b99ac2df0c6
}
