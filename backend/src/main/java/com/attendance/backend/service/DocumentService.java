package com.attendance.backend.service;

import com.attendance.backend.entity.StudyDocument;
import com.attendance.backend.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public StudyDocument addDocument(StudyDocument document) {
        document.setUploadedAt(LocalDateTime.now());
        return documentRepository.save(document);
    }

    public List<StudyDocument> getAllDocuments() {
        return documentRepository.findAll();
    }

    public void deleteDocument(String id) {
        documentRepository.deleteById(id);
    }
}
