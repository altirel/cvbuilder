package com.basiliqo.cvbuilder.service.impl;

import com.basiliqo.cvbuilder.configuration.MinioProperties;
import com.basiliqo.cvbuilder.enums.DocumentType;
import com.basiliqo.cvbuilder.exception.UnsupportedDocumentTypeException;
import com.basiliqo.cvbuilder.service.MinioService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service(MinioService.NAME)
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public MinioServiceImpl(MinioClient minioClient,
                            MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    public String getBucketNameByDocumentType(DocumentType documentType) {
        if (documentType == DocumentType.TEMPLATE) {
            return minioProperties.getTemplatesBucket();
        }
        if (documentType == DocumentType.DOCUMENT) {
            return minioProperties.getDocumentsBucket();
        }
        log.error("Unsupported document type to identify bucket name");
        throw new UnsupportedDocumentTypeException("Unsupported document type.");
    }

    public boolean bucketExists(String bucketName) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    public void createBucket(String bucketName) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }
}
