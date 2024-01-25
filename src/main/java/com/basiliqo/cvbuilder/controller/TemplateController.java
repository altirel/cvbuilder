package com.basiliqo.cvbuilder.controller;

import com.basiliqo.cvbuilder.dto.TemplateDetailedResponse;
import com.basiliqo.cvbuilder.dto.TemplatePageResponse;
import com.basiliqo.cvbuilder.dto.TemplateSaveRequest;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.service.DocumentFileService;
import com.basiliqo.cvbuilder.service.TemplateService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/templates")
public record TemplateController(TemplateService templateService,
                                 DocumentFileService documentFileService) {

    @GetMapping("/coverletters/{id}")
    public ResponseEntity<TemplateDetailedResponse> getCoverLetterById(@PathVariable("id") String id) {
        return ResponseEntity.ok(templateService.loadByIdAndType(id, DocumentContentType.COVER_LETTER));
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<TemplateDetailedResponse> getResumeById(@PathVariable("id") String id) {
        return ResponseEntity.ok(templateService.loadByIdAndType(id, DocumentContentType.RESUME));
    }

    @GetMapping("/coverletters")
    public ResponseEntity<TemplatePageResponse> getAllRecentCoverLetters(@RequestParam Integer page,
                                                                         @RequestParam Integer limit) {
        return ResponseEntity.ok(templateService.loadAllByType(DocumentContentType.COVER_LETTER, page, limit));
    }

    @GetMapping("/resumes")
    public ResponseEntity<TemplatePageResponse> getAllRecentResumes(@RequestParam Integer page,
                                                                    @RequestParam Integer limit) {
        return ResponseEntity.ok(templateService.loadAllByType(DocumentContentType.RESUME, page, limit));
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadTemplate(@Valid TemplateSaveRequest request) {
        return templateService.save(request);
    }

}
