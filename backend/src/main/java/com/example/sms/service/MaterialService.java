package com.example.sms.service;

import com.example.sms.dto.response.MaterialResponse;
import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final ClassSubjectRepository classSubjectRepository;
    private final FileStorageService fileStorageService;
    private final UserContextService userContextService;

    public MaterialService(MaterialRepository materialRepository,
                           ClassSubjectRepository classSubjectRepository,
                           FileStorageService fileStorageService,
                           UserContextService userContextService) {
        this.materialRepository = materialRepository;
        this.classSubjectRepository = classSubjectRepository;
        this.fileStorageService = fileStorageService;
        this.userContextService = userContextService;
    }

    @Transactional
    public MaterialResponse uploadMaterial(Long classSubjectId, String title, String description, MultipartFile file) throws IOException {
        Teacher teacher = userContextService.getCurrentTeacher();
        ClassSubject cs = classSubjectRepository.findById(classSubjectId)
                .orElseThrow(() -> new RuntimeException("Class-Subject mapping not found"));

        if (!cs.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("You are not authorized to upload material for this subject/class");
        }

        String filename = fileStorageService.storeFile(file);
        
        Material material = new Material();
        material.setTitle(title);
        material.setDescription(description);
        material.setFileUrl(filename);
        material.setFileType(file.getContentType());
        material.setClassSubject(cs);
        material.setUploadedBy(teacher);
        
        return mapToResponse(materialRepository.save(material));
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMaterialsForClass(Long classId) {
        // Students or Advisors might call this
        return materialRepository.findAll().stream()
                .filter(m -> m.getClassSubject().getClassEntity().getId().equals(classId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMyMaterials() {
        Teacher teacher = userContextService.getCurrentTeacher();
        return materialRepository.findByUploadedById(teacher.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMaterial(Long id) throws IOException {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));
        
        Teacher teacher = userContextService.getCurrentTeacher();
        if (!material.getUploadedBy().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized to delete this material");
        }

        fileStorageService.deleteFile(material.getFileUrl());
        materialRepository.delete(material);
    }

    private MaterialResponse mapToResponse(Material m) {
        MaterialResponse resp = new MaterialResponse();
        resp.setId(m.getId());
        resp.setTitle(m.getTitle());
        resp.setDescription(m.getDescription());
        resp.setFileUrl(m.getFileUrl());
        resp.setFileType(m.getFileType());
        resp.setSubjectName(m.getClassSubject().getSubject().getName());
        resp.setClassName(m.getClassSubject().getClassEntity().getName());
        resp.setUploadedAt(m.getUploadedAt());
        return resp;
    }
}
