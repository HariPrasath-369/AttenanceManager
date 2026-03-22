package com.example.sms.service;

import com.example.sms.dto.response.SuggestionResponse;
import com.example.sms.entity.Hod;
import com.example.sms.entity.Student;
import com.example.sms.entity.Suggestion;
import com.example.sms.repository.SuggestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionService {
    private final SuggestionRepository suggestionRepository;
    private final UserContextService userContextService;

    public SuggestionService(SuggestionRepository suggestionRepository,
                             UserContextService userContextService) {
        this.suggestionRepository = suggestionRepository;
        this.userContextService = userContextService;
    }

    @Transactional
    public void submitSuggestion(String content) {
        Student student = userContextService.getCurrentStudent();
        Hod hod = student.getClassEntity().getDepartment().getHod();
        
        Suggestion suggestion = new Suggestion();
        suggestion.setContent(content);
        suggestion.setStudent(student);
        suggestion.setHod(hod);
        suggestionRepository.save(suggestion);
    }

    @Transactional(readOnly = true)
    public List<SuggestionResponse> getSuggestionsForCurrentHod() {
        Hod hod = userContextService.getCurrentHod();
        return suggestionRepository.findByHodIdOrderByCreatedAtDesc(hod.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SuggestionResponse mapToResponse(Suggestion s) {
        SuggestionResponse resp = new SuggestionResponse();
        resp.setId(s.getId());
        resp.setContent(s.getContent());
        resp.setStudentName(s.getStudent().getUser().getFullName());
        resp.setCreatedAt(s.getCreatedAt().toString());
        resp.setStatus(s.getStatus());
        return resp;
    }
}
