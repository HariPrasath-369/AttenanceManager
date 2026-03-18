package com.attendance.backend.repository;

import com.attendance.backend.entity.StudyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends MongoRepository<StudyDocument, String> {
}
