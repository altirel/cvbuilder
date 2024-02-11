package com.basiliqo.cvbuilder.service;

import com.basiliqo.cvbuilder.exception.FileProcessingException;
import com.basiliqo.cvbuilder.service.impl.PdfServiceImpl;
import com.google.common.io.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PdfServiceTest {

    private PdfService pdfService;

    @BeforeEach
    public void beforeEach() {
        pdfService = new PdfServiceImpl();
    }

    @Test
    public void extractParameters_shouldPass() throws IOException {
        // Preparation
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MultipartFile file = new MockMultipartFile("Test Resume",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content);

        // Call
        List<String> parameters = pdfService.extractParameters(file);

        // Assertions
        assertNotNull(parameters, "parameters are null");
        assertEquals(2, parameters.size());
        assertEquals("PARAMETER_1", parameters.get(0));
        assertEquals("SHOULD_PARAM", parameters.get(1));
    }

    @Test
    public void extractParameters_shouldThrowsFileProcessingException() throws IOException {
        // Preparation
        byte[] content = Resources.toByteArray(getClass().getResource("/document/corrupted_resume.pdf"));
        MultipartFile file = new MockMultipartFile("Test Resume",
                "corrupted_resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content);

        // Call
        assertThrows(FileProcessingException.class, () -> pdfService.extractParameters(file));
    }
}
