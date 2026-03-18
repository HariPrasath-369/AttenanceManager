package com.attendance.backend.controller;

import com.attendance.backend.entity.StudyDocument;
import com.attendance.backend.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/public/documents")
    public ResponseEntity<List<StudyDocument>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @PostMapping("/documents")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StudyDocument> addDocument(@RequestBody StudyDocument document) {
        return ResponseEntity.ok(documentService.addDocument(document));
    }

    @DeleteMapping("/documents/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
