package com.basiliqo.cvbuilder.mapper;

import com.basiliqo.cvbuilder.dto.TemplateDetailedResponse;
import com.basiliqo.cvbuilder.dto.TemplateMinimalResponse;
import com.basiliqo.cvbuilder.dto.TemplatePageResponse;
import com.basiliqo.cvbuilder.entity.FileDetails;
import com.basiliqo.cvbuilder.entity.Template;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.FileFormat;
import com.basiliqo.cvbuilder.mapper.impl.TemplateMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TemplateMapperTest {

    private TemplateMapper templateMapper;

    @BeforeEach
    public void beforeEach() {
        templateMapper = new TemplateMapperImpl();
    }

    @Test
    public void toDetailedResponse_shouldPass() {
        // Preparations
        Template template = new Template("Test template",
                DocumentContentType.RESUME,
                new FileDetails("resume.pdf", "/resumes/resume.pdf", "templates", FileFormat.PDF),
                List.of("PARAMETER_1", "SHOULD_PARAM"),
                LocalDateTime.of(2024, 1, 28, 10, 58, 27));
        template.setId("127512490174");

        // Call
        TemplateDetailedResponse actual = templateMapper.toDetailedResponse(template);

        // Assertions
        assertEquals(template.getId(), actual.id());
        assertEquals(template.getName(), actual.name());
        assertEquals(template.getDocumentContentType().getId(), actual.type());
        assertEquals(template.getCreated(), actual.created());
        assertEquals(template.getFileDetails().getFileFormat().getId(), actual.fileFormat());
        assertEquals(template.getParameters(), actual.parameters());
    }

    @Test
    public void toMinimalResponse_shouldPass() {
        // Preparations
        Template template = new Template("Test template",
                DocumentContentType.RESUME,
                new FileDetails("resume.pdf", "/resumes/resume.pdf", "templates", FileFormat.PDF),
                List.of("PARAMETER_1", "SHOULD_PARAM"),
                LocalDateTime.of(2024, 1, 28, 10, 58, 27));
        template.setId("127512490174");

        // Call
        TemplateMinimalResponse actual = templateMapper.toMinimalResponse(template);

        // Assertions
        assertEquals(template.getId(), actual.id());
        assertEquals(template.getName(), actual.name());
        assertEquals(template.getDocumentContentType().getId(), actual.type());
        assertEquals(template.getCreated(), actual.created());
    }

    @Test
    public void toTemplatePageResponse_shouldPass() {
        // Preparations
        Template template1 = new Template("Test template 1",
                DocumentContentType.RESUME,
                new FileDetails("resume_1.pdf", "/resumes/resume_1.pdf", "templates", FileFormat.PDF),
                List.of("PARAMETER_1", "SHOULD_PARAM"),
                LocalDateTime.of(2024, 1, 28, 10, 58, 27));
        template1.setId("127512490174");

        Template template2 = new Template("Test template 2",
                DocumentContentType.COVER_LETTER,
                new FileDetails("resume_2.pdf", "/resumes/resume_2.pdf", "templates", FileFormat.PDF),
                List.of("PARAMETER_2", "SHOULD_PARAM"),
                LocalDateTime.of(2024, 1, 28, 10, 58, 30));
        template2.setId("127512490175");

        List<Template> templates = List.of(template1, template2);

        Pageable pageable = PageRequest.of(0, 2, Sort.by("created").descending());
        Page<Template> page = new PageImpl<>(templates, pageable, 4);

        // Call
        TemplatePageResponse actual = templateMapper.toTemplatePageResponse(page);

        // Assertions
        assertEquals(page.getTotalPages(), actual.maxPages());
        assertEquals(page.getNumber(), actual.page());
        assertEquals(page.getSize(), actual.size());
        assertEquals(templates.size(), actual.templates().size());
    }
}