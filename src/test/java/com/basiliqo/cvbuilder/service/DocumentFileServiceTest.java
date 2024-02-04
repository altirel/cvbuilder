package com.basiliqo.cvbuilder.service;

import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import com.basiliqo.cvbuilder.exception.FileSavingException;
import com.basiliqo.cvbuilder.mapper.FileDataMapper;
import com.basiliqo.cvbuilder.service.impl.DocumentFileServiceImpl;
import com.google.common.io.Resources;
import io.minio.ObjectWriteResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DocumentFileServiceTest {

    private DocumentFileService documentFileService;

    @Mock
    private MinioService minioService;
    @Mock
    private FileDataMapper fileDataMapper;

    @BeforeEach
    public void beforeEach() {
        documentFileService = new DocumentFileServiceImpl(minioService, fileDataMapper);
    }

    @Test
    public void saveTemplateFile_shouldPass() throws IOException, ServerException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        // Preparations
        String bucketName = "templates";
        DocumentContentType documentContentType = DocumentContentType.RESUME;

        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MultipartFile file = new MockMultipartFile("Test Resume",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content);

        String filepath = "/resumes/resume.pdf";
        ObjectWriteResponse objectWriteResponse = new ObjectWriteResponse(null, bucketName, "US",
                filepath, null, null);
        FileSaveResponse expected = new FileSaveResponse(bucketName, filepath);

        when(minioService.getBucketNameByDocumentType(any(DocumentType.class))).thenReturn(bucketName);
        when(minioService.bucketExists(bucketName)).thenReturn(false);
        when(minioService.saveFile(file, documentContentType, bucketName)).thenReturn(objectWriteResponse);
        when(fileDataMapper.toFileSaveResponse(objectWriteResponse)).thenReturn(expected);

        // Call
        FileSaveResponse actual = documentFileService
                .saveTemplateFile(file, DocumentType.TEMPLATE, DocumentContentType.RESUME);

        // Assertions
        assertEquals(expected, actual);
        verify(minioService, times(1)).createBucket(bucketName);
    }

    @Test
    public void saveTemplateFile_shouldThrowsFileSavingException_onNullBucketName() throws IOException {
        // Preparations
        DocumentContentType documentContentType = DocumentContentType.RESUME;
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MultipartFile file = new MockMultipartFile("Test Resume",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content);

        // Assertions
        assertThrows(FileSavingException.class,
                () -> documentFileService.saveTemplateFile(file, null, documentContentType));
    }

    @Test
    public void saveTemplateFile_shouldThrowsFileSavingException_onServerException() throws IOException,
            ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MultipartFile file = new MockMultipartFile("Test Resume",
                "resume.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content);

        when(minioService.getBucketNameByDocumentType(any(DocumentType.class))).thenReturn("templates");
        when(minioService.bucketExists(any(String.class))).thenThrow(ServerException.class);

        // Assertions
        assertThrows(FileSavingException.class,
                () -> documentFileService.saveTemplateFile(file, DocumentType.TEMPLATE, DocumentContentType.RESUME));
    }
}
