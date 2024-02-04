package com.basiliqo.cvbuilder.service;

import com.basiliqo.cvbuilder.configuration.MinioProperties;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import com.basiliqo.cvbuilder.exception.FileSavingException;
import com.basiliqo.cvbuilder.exception.UnsupportedDocumentTypeException;
import com.basiliqo.cvbuilder.service.impl.MinioServiceImpl;
import com.google.common.io.Resources;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MinioServiceTest {

    private MinioService minioService;

    @Mock
    private MinioProperties minioProperties;
    @Mock
    private MinioClient minioClient;

    @BeforeEach
    public void setUp() {
        minioService = new MinioServiceImpl(minioClient, minioProperties);
    }

    @Test
    public void getBucketNameByDocumentType_shouldPass_onDocument() {
        // Preparations
        when(minioProperties.getDocumentsBucket()).thenReturn("documents");

        // Call
        String bucketName = minioService.getBucketNameByDocumentType(DocumentType.DOCUMENT);

        // Assertions
        assertEquals("documents", bucketName);
    }

    @Test
    public void getBucketNameByDocumentType_shouldPass_onTemplate() {
        // Preparations
        when(minioProperties.getTemplatesBucket()).thenReturn("templates");

        // Call
        String bucketName = minioService.getBucketNameByDocumentType(DocumentType.TEMPLATE);

        // Assertions
        assertEquals("templates", bucketName);
    }

    @Test
    public void getBucketNameByDocumentType_shouldThrowsUnsupportedDocumentTypeException() {
        assertThrows(UnsupportedDocumentTypeException.class, () -> minioService.getBucketNameByDocumentType(null));
    }

    @Test
    public void bucketExists_shouldPass_onTrue() throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        // Preparations
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        // Call
        boolean exists = minioService.bucketExists("templates");

        // Assertions
        assertTrue(exists);
    }

    @Test
    public void bucketExists_shouldPass_onFalse() throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        // Preparations
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);

        // Call
        boolean exists = minioService.bucketExists("unknown");

        // Assertions
        assertFalse(exists);
    }

    @Test
    public void createBucket_shouldPass() throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
            InternalException {
        // Call
        minioService.createBucket("templates");

        // Assertions
        verify(minioClient, times(1)).makeBucket(any(MakeBucketArgs.class));
    }

    @Test
    public void saveFile_shouldPass() throws IOException, ServerException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MultipartFile file = new MockMultipartFile("Test Resume",
                "resume,pdf",
                MediaType.TEXT_PLAIN_VALUE,
                content);

        ObjectWriteResponse expected = new ObjectWriteResponse(null,
                "templates",
                "US",
                "/path/resume.pdf",
                null,
                null);
        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(expected);

        // Call
        ObjectWriteResponse actual = minioService
                .saveFile(file, DocumentContentType.RESUME, "templates");

        // Assertions
        assertEquals(expected, actual);
    }

    @Test
    public void saveFile_shouldThrowFileSavingException() throws IOException {
        // Preparations
        byte[] content = Resources.toByteArray(getClass().getResource("/document/resume.pdf"));
        MultipartFile file = new MockMultipartFile("Test Resume",
                null,
                MediaType.TEXT_PLAIN_VALUE,
                content);

        // Assertions
        assertThrows(FileSavingException.class,
                () -> minioService.saveFile(file, DocumentContentType.RESUME, "templates"));
    }
}
