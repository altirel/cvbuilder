package com.basiliqo.cvbuilder.component.impl;

import com.basiliqo.cvbuilder.component.TemplateParameterExtractor;
import com.basiliqo.cvbuilder.enums.FileFormat;
import com.basiliqo.cvbuilder.exception.UnsupportedFileFormatException;
import com.basiliqo.cvbuilder.service.DocxService;
import com.basiliqo.cvbuilder.service.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class TemplateParameterExtractorTest {

    private TemplateParameterExtractor templateParameterExtractor;

    @Mock
    private PdfService pdfService;
    @Mock
    private DocxService docxService;

    @BeforeEach
    public void beforeEach() {
        templateParameterExtractor = new TemplateParameterExtractorImpl(pdfService, docxService);
    }

    @Test
    public void extractParameters_shouldPass_onPdf() throws IOException {
        // Preparations
        MultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                (InputStream) null
        );

        List<String> expected = List.of("PARAMETER_1", "SHOULD_PARAM");
        when(pdfService.extractParameters(file)).thenReturn(expected);

        // Call
        List<String> actual = templateParameterExtractor.extractParameters(file, FileFormat.PDF);

        // Assertions
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void extractParameters_shouldPass_onDocx() throws IOException {
        // Preparations
        MultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                (InputStream) null
        );

        List<String> expected = List.of("PARAMETER_1", "SHOULD_PARAM");
        when(docxService.extractParameters(file)).thenReturn(expected);

        // Call
        List<String> actual = templateParameterExtractor.extractParameters(file, FileFormat.DOCX);

        // Assertions
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void extractParameters_shouldThrowUnsupportedFileFormatException() throws IOException {
        // Preparations
        MultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                (InputStream) null
        );

        // Assertions
        assertThrows(UnsupportedFileFormatException.class,
                () -> templateParameterExtractor.extractParameters(file, null));
    }
}