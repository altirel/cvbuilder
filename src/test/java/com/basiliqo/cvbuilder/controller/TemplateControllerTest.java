package com.basiliqo.cvbuilder.controller;

import com.basiliqo.cvbuilder.entity.FileDetails;
import com.basiliqo.cvbuilder.entity.Template;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import com.basiliqo.cvbuilder.enums.FileFormat;
import com.basiliqo.cvbuilder.repository.TemplateRepository;
import com.basiliqo.cvbuilder.service.MinioService;
import com.google.common.io.Resources;
import io.minio.ObjectWriteResponse;
import io.minio.errors.ServerException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.yml")
class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MinioService minioService;
    @MockBean
    private TemplateRepository templateRepository;

    @Test
    public void getCoverLetterById_shouldPass() throws Exception {
        // Preparations
        String id = "176824901";
        Template template = createTemplate(id, "Test template", DocumentContentType.COVER_LETTER,
                "/coverletters/cl.pdf", FileFormat.PDF);

        when(templateRepository.findByIdAndDocumentContentType(id, DocumentContentType.COVER_LETTER))
                .thenReturn(Optional.of(template));

        // Call & Assertions
        mockMvc.perform(get("/api/v1/templates/coverletters/176824901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Test template"))
                .andExpect(jsonPath("$.type").value("Cover letter"))
                .andExpect(jsonPath("$.file_format").value("pdf"));
    }

    @Test
    public void getResumeById_shouldPass() throws Exception {
        // Preparations
        String id = "176824901";
        Template template = createTemplate(id, "Test template", DocumentContentType.RESUME,
                "/resumes/resume.pdf", FileFormat.PDF);

        when(templateRepository.findByIdAndDocumentContentType(id, DocumentContentType.RESUME))
                .thenReturn(Optional.of(template));

        // Call & Assertions
        mockMvc.perform(get("/api/v1/templates/resumes/176824901").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Test template"))
                .andExpect(jsonPath("$.type").value("Resume"))
                .andExpect(jsonPath("$.file_format").value("pdf"));
    }

    @Test
    public void getResumeById_shouldThrowEntityNotFoundException() throws Exception {
        // Preparations
        String id = "176824901";

        when(templateRepository.findByIdAndDocumentContentType(id, DocumentContentType.RESUME))
                .thenReturn(Optional.empty());

        // Call & Assertions
        mockMvc.perform(get("/api/v1/templates/resumes/176824901").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.reason").value("Not Found"))
                .andExpect(jsonPath("$.detail").value("Template not found."));
    }

    @Test
    public void getAllRecentCoverLetters_shouldPass() throws Exception {
        // Preparations
        String id1 = "176824901";
        Template template1 = createTemplate(id1, "Test template 1", DocumentContentType.COVER_LETTER,
                "/coverletters/cl1.pdf", FileFormat.PDF);
        String id2 = "176824912";
        Template template2 = createTemplate(id2, "Test template 2", DocumentContentType.COVER_LETTER,
                "/coverletters/cl2.pdf", FileFormat.PDF);
        List<Template> templates = List.of(template1, template2);

        Page<Template> page = new PageImpl<>(templates, PageRequest.of(0, 2), 4);
        when(templateRepository.findAllByDocumentContentType(any(DocumentContentType.class), any(Pageable.class)))
                .thenReturn(page);

        // Call & Assertions
        mockMvc.perform(get("/api/v1/templates/coverletters")
                        .param("page", "0")
                        .param("limit", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.templates.[0].id").value(id1))
                .andExpect(jsonPath("$.templates.[0].name").value("Test template 1"))
                .andExpect(jsonPath("$.templates.[0].type").value("Cover letter"))
                .andExpect(jsonPath("$.templates.[1].id").value(id2))
                .andExpect(jsonPath("$.templates.[1].name").value("Test template 2"))
                .andExpect(jsonPath("$.templates.[1].type").value("Cover letter"));
    }

    @Test
    public void getAllRecentResumes_shouldPass() throws Exception {
        // Preparations
        String id1 = "176824901";
        Template template1 = createTemplate(id1, "Test template 1", DocumentContentType.RESUME,
                "/coverletters/resume1.pdf", FileFormat.PDF);
        String id2 = "176824912";
        Template template2 = createTemplate(id2, "Test template 2", DocumentContentType.RESUME,
                "/coverletters/resume2.pdf", FileFormat.PDF);
        List<Template> templates = List.of(template1, template2);

        Page<Template> page = new PageImpl<>(templates, PageRequest.of(0, 2), 4);
        when(templateRepository.findAllByDocumentContentType(any(DocumentContentType.class), any(Pageable.class)))
                .thenReturn(page);

        // Call & Assertions
        mockMvc.perform(get("/api/v1/templates/resumes")
                        .param("page", "0")
                        .param("limit", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.templates.[0].id").value(id1))
                .andExpect(jsonPath("$.templates.[0].name").value("Test template 1"))
                .andExpect(jsonPath("$.templates.[0].type").value("Resume"))
                .andExpect(jsonPath("$.templates.[1].id").value(id2))
                .andExpect(jsonPath("$.templates.[1].name").value("Test template 2"))
                .andExpect(jsonPath("$.templates.[1].type").value("Resume"));
    }

    @Test
    public void uploadTemplate_shouldPass() throws Exception {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content
        );

        String id = "16823957592";
        ObjectWriteResponse savedFile =
                new ObjectWriteResponse(null, "templates", "US", "/resumes/resume.pdf", null, null);
        FileDetails fileDetails = new FileDetails("resume.pdf", "/resumes/resume.pdf", "templates", FileFormat.PDF);
        List<String> parameters = List.of("PARAMETER_1", "SHOULD_PARAM");
        Template template =
                new Template("Test template", DocumentContentType.RESUME, fileDetails, parameters, LocalDateTime.now());
        template.setId(id);

        when(minioService.bucketExists("templates")).thenReturn(true);
        when(minioService.saveFile(file, DocumentContentType.RESUME, "templates"))
                .thenReturn(savedFile);
        when(minioService.getBucketNameByDocumentType(DocumentType.TEMPLATE)).thenReturn("templates");

        when(templateRepository.existsByName("Test template")).thenReturn(false);
        when(templateRepository.save(any(Template.class))).thenReturn(template);

        // Call & Assertions
        mockMvc.perform(multipart("/api/v1/templates/upload")
                        .file(file)
                        .param("name", "Test template")
                        .param("type", "Resume"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string(id));
    }

    @Test
    public void uploadTemplate_shouldThrowUnsupportedDocumentTypeException() throws Exception {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content
        );

        // Call & Assertions
        mockMvc.perform(multipart("/api/v1/templates/upload")
                        .file(file)
                        .param("name", "Test template")
                        .param("type", "wrong type"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(415))
                .andExpect(jsonPath("$.reason").value("Unsupported Media Type"))
                .andExpect(jsonPath("$.detail").value("Document content type is not supported"));
    }

    @Test
    public void uploadTemplate_shouldThrowWrongNameException() throws Exception {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content
        );

        String name = "Test template";
        when(templateRepository.existsByName(name)).thenReturn(true);

        // Call & Assertions
        mockMvc.perform(multipart("/api/v1/templates/upload")
                        .file(file)
                        .param("name", name)
                        .param("type", "Resume"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.reason").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("Name is already taken"));
    }

    @Test
    public void uploadTemplate_shouldThrowFileSavingException() throws Exception {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content
        );

        String name = "Test template";
        when(templateRepository.existsByName(name)).thenReturn(false);
        when(minioService.getBucketNameByDocumentType(DocumentType.TEMPLATE)).thenReturn("templates");
        when(minioService.saveFile(file, DocumentContentType.RESUME, "templates"))
                .thenThrow(ServerException.class);

        // Call & Assertions
        mockMvc.perform(multipart("/api/v1/templates/upload")
                        .file(file)
                        .param("name", name)
                        .param("type", "Resume"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.reason").value("Internal Server Error"))
                .andExpect(jsonPath("$.detail")
                        .value("Error occurred while saving a file in MinIO"));
    }

    @Test
    public void uploadTemplate_shouldThrowUnsupportedFileFormatException() throws Exception {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/corrupted_resume.pdf"));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.png",
                MediaType.TEXT_PLAIN_VALUE,
                content
        );

        String name = "Test template";
        ObjectWriteResponse objectWriteResponse =
                new ObjectWriteResponse(null, "templates", "US", "/templates/resumed.png", null, null);
        when(templateRepository.existsByName(name)).thenReturn(false);
        when(minioService.getBucketNameByDocumentType(DocumentType.TEMPLATE)).thenReturn("templates");
        when(minioService.saveFile(file, DocumentContentType.RESUME, "templates"))
                .thenReturn(objectWriteResponse);

        // Call & Assertions
        mockMvc.perform(multipart("/api/v1/templates/upload")
                        .file(file)
                        .param("name", name)
                        .param("type", "Resume"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(415))
                .andExpect(jsonPath("$.reason").value("Unsupported Media Type"))
                .andExpect(jsonPath("$.detail").value("File format is not supported"));
    }

    @Test
    public void uploadTemplate_shouldThrowFileProcessingException() throws Exception {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/corrupted_resume.pdf"));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content
        );

        String name = "Test template";
        ObjectWriteResponse objectWriteResponse =
                new ObjectWriteResponse(null, "templates", "US", "/templates/resumed.pdf", null, null);
        when(templateRepository.existsByName(name)).thenReturn(false);
        when(minioService.getBucketNameByDocumentType(DocumentType.TEMPLATE)).thenReturn("templates");
        when(minioService.saveFile(file, DocumentContentType.RESUME, "templates"))
                .thenReturn(objectWriteResponse);

        // Call & Assertions
        mockMvc.perform(multipart("/api/v1/templates/upload")
                        .file(file)
                        .param("name", name)
                        .param("type", "Resume"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(415))
                .andExpect(jsonPath("$.reason").value("Unsupported Media Type"))
                .andExpect(jsonPath("$.detail").value("Error occurred while parsing parameters" +
                        " from template file. Closing bracket not found"));
    }

    private Template createTemplate(String id,
                                    String name,
                                    DocumentContentType documentContentType,
                                    String filepath,
                                    FileFormat fileFormat) {
        Template template = new Template(name,
                documentContentType,
                new FileDetails("file", filepath, "templates", fileFormat),
                List.of("PARAMETER_1", "SHOULD_PARAM"),
                LocalDateTime.of(2024, 1, 28, 13, 24, 10));
        template.setId(id);
        return template;
    }
}