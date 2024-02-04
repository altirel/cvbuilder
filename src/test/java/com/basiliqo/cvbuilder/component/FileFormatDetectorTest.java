package com.basiliqo.cvbuilder.component;

import com.basiliqo.cvbuilder.component.impl.FileFormatDetectorImpl;
import com.basiliqo.cvbuilder.enums.FileFormat;
import com.basiliqo.cvbuilder.exception.UnsupportedFileFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FileFormatDetectorTest {

    private FileFormatDetector fileFormatDetector;

    @BeforeEach
    public void beforeEach() {
        fileFormatDetector = new FileFormatDetectorImpl();
    }

    @Test
    public void detectFileFormat_shouldPass_onPdf() {
        // Preparations
        FileFormat expected = FileFormat.PDF;

        // Call
        FileFormat actual = fileFormatDetector.detectFileFormat("resume.pdf");

        // Assertions
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void detectFileFormat_shouldPass_onDocx() {
        // Preparations
        FileFormat expected = FileFormat.DOCX;

        // Call
        FileFormat actual = fileFormatDetector.detectFileFormat("resume.docx");

        // Assertions
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void detectFileFormat_shouldThrowUnsupportedFileFormatException_onWrongFilename() {
        // Assertions
        assertThrows(UnsupportedFileFormatException.class,
                () -> fileFormatDetector.detectFileFormat("resume.zip"));
    }

    @Test
    public void detectFileFormat_shouldThrowUnsupportedFileFormatException_onNull() {
        // Assertions
        assertThrows(UnsupportedFileFormatException.class,
                () -> fileFormatDetector.detectFileFormat(null));
    }
}