package com.basiliqo.cvbuilder.service;

import com.basiliqo.cvbuilder.component.FileFormatDetector;
import com.basiliqo.cvbuilder.component.TemplateParameterExtractor;
import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import com.basiliqo.cvbuilder.dto.TemplateDetailedResponse;
import com.basiliqo.cvbuilder.dto.TemplateMinimalResponse;
import com.basiliqo.cvbuilder.dto.TemplatePageResponse;
import com.basiliqo.cvbuilder.dto.TemplateSaveRequest;
import com.basiliqo.cvbuilder.entity.FileDetails;
import com.basiliqo.cvbuilder.entity.Template;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import com.basiliqo.cvbuilder.enums.FileFormat;
import com.basiliqo.cvbuilder.exception.EntityNotFoundException;
import com.basiliqo.cvbuilder.exception.FileProcessingException;
import com.basiliqo.cvbuilder.exception.WrongNameException;
import com.basiliqo.cvbuilder.mapper.TemplateMapper;
import com.basiliqo.cvbuilder.repository.TemplateRepository;
import com.basiliqo.cvbuilder.service.impl.TemplateServiceImpl;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TemplateServiceTest {

    private TemplateServiceImpl templateService;

    @Mock
    private TemplateMapper templateMapper;
    @Mock
    private FileFormatDetector fileFormatDetector;
    @Mock
    private TemplateRepository templateRepository;
    @Mock
    private DocumentFileService documentFileService;
    @Mock
    private TemplateParameterExtractor templateParameterExtractor;

    @BeforeEach
    public void beforeEach() {
        templateService = new TemplateServiceImpl(
                templateMapper,
                fileFormatDetector,
                templateRepository,
                documentFileService,
                templateParameterExtractor
        );
    }

    @Test
    public void loadAllByType_shouldPass() {
        // Preparations
        DocumentContentType documentContentType = DocumentContentType.RESUME;
        List<Template> templates = List.of(
                new Template(null, null, null, null, null),
                new Template(null, null, null, null, null)
        );
        List<TemplateMinimalResponse> templateMinimalResponses = List.of(
                new TemplateMinimalResponse(null, null, null, null),
                new TemplateMinimalResponse(null, null, null, null)
        );

        Page<Template> page = new PageImpl<>(templates, Pageable.ofSize(2), 2);
        TemplatePageResponse expected = new TemplatePageResponse(templateMinimalResponses, 0, 2, 1);

        when(templateRepository.findAllByDocumentContentType(any(DocumentContentType.class), any(PageRequest.class)))
                .thenReturn(page);
        when(templateMapper.toTemplatePageResponse(page)).thenReturn(expected);


        // Call
        TemplatePageResponse actual = templateService.loadAllByType(documentContentType, 0, 2);

        // Assertions
        assertEquals(2, actual.templates().size());
        assertEquals(0, actual.page());
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void loadByIdAndType_shouldPass() {
        // Preparations
        String id = "127512490174";
        DocumentContentType documentContentType = DocumentContentType.RESUME;
        Template template = new Template("Test Template", documentContentType, null, null, null);
        TemplateDetailedResponse expected =
                new TemplateDetailedResponse(id, template.getName(), documentContentType.getId(), null, null, null);

        when(templateRepository.findByIdAndDocumentContentType(id, documentContentType))
                .thenReturn(Optional.of(template));
        when(templateMapper.toDetailedResponse(template)).thenReturn(expected);

        // Call
        TemplateDetailedResponse actual = templateService.loadByIdAndType(id, documentContentType);

        // Assertions
        assertEquals(expected, actual);
    }

    @Test
    public void loadByIdAndType_shouldThrowsEntityNotFoundException() {
        // Preparations
        String id = "127512490174";
        DocumentContentType documentContentType = DocumentContentType.RESUME;
        when(templateRepository.findByIdAndDocumentContentType(id, documentContentType))
                .thenReturn(Optional.empty());

        // Assertion
        assertThrows(EntityNotFoundException.class, () -> templateService.loadByIdAndType(id, documentContentType));
    }

    @Test
    public void save_shouldPass() throws IOException {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MultipartFile file = new MockMultipartFile("file", "resume.pdf", MediaType.TEXT_PLAIN_VALUE, content);

        String name = "Test resume";
        String bucket = "templates";
        String filepath = "resumes/2024-01-28-10:17:18_resume.pdf";
        TemplateSaveRequest saveRequest = new TemplateSaveRequest(name, "Resume", file);
        FileSaveResponse savedFile = new FileSaveResponse(bucket, filepath);

        String expected = "127512490174";
        Template template = new Template(name,
                DocumentContentType.RESUME,
                new FileDetails(file.getOriginalFilename(), filepath, bucket, FileFormat.PDF),
                List.of("PARAMETER_1", "SHOULD_PARAM"),
                LocalDateTime.of(2024, 1, 28, 10, 17, 18)
        );
        template.setId(expected);

        when(templateRepository.existsByName(name)).thenReturn(false);
        when(documentFileService.saveTemplateFile(file, DocumentType.TEMPLATE, DocumentContentType.RESUME))
                .thenReturn(savedFile);
        when(fileFormatDetector.detectFileFormat(file.getOriginalFilename())).thenReturn(FileFormat.PDF);
        when(templateParameterExtractor.extractParameters(file, FileFormat.PDF))
                .thenReturn(List.of("PARAMETER_1", "SHOULD_PARAM"));
        when(templateRepository.save(any(Template.class))).thenReturn(template);

        // Call
        String actual = templateService.save(saveRequest);

        // Assertions
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void save_shouldThrowsWrongNameException_onExists() throws IOException {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MultipartFile file = new MockMultipartFile("file", "resume.pdf", MediaType.TEXT_PLAIN_VALUE, content);
        String name = "Test template";
        TemplateSaveRequest saveRequest = new TemplateSaveRequest(name, "Resume", file);

        when(templateRepository.existsByName(name)).thenReturn(true);

        // Assertions
        Assertions.assertThrows(WrongNameException.class, () -> templateService.save(saveRequest));
    }

    @Test
    public void save_shouldThrowsFileProcessingException() throws IOException {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MultipartFile file = new MockMultipartFile("file", "resume.pdf", MediaType.TEXT_PLAIN_VALUE, content);

        String name = "Test resume";
        TemplateSaveRequest saveRequest = new TemplateSaveRequest(name, "Resume", file);

        when(templateRepository.existsByName(name)).thenReturn(false);
        when(fileFormatDetector.detectFileFormat(file.getOriginalFilename())).thenReturn(FileFormat.PDF);
        when(templateParameterExtractor.extractParameters(file, FileFormat.PDF)).thenThrow(IOException.class);

        // Assertions
        assertThrows(FileProcessingException.class, () -> templateService.save(saveRequest));
    }
}
