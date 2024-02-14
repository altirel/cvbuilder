package com.basiliqo.cvbuilder.service.impl;

import com.basiliqo.cvbuilder.configuration.MinioProperties;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import com.basiliqo.cvbuilder.exception.FileSavingException;
import com.basiliqo.cvbuilder.exception.UnsupportedDocumentTypeException;
import com.basiliqo.cvbuilder.service.MinioService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service(MinioService.NAME)
public class MinioServiceImpl implements MinioService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh:mm:ss");

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

    @Override
    public ObjectWriteResponse saveFile(MultipartFile file,
                                        DocumentContentType documentContentType,
                                        String bucketName) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new FileSavingException("No filename provided.");
        }

        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(generateFilepath(documentContentType.getId(), file.getOriginalFilename()))
                .stream(file.getInputStream(), file.getSize(), -1)
                .build();
        return minioClient.putObject(putObjectArgs);
    }

    @Nonnull
    private String generateFilepath(@Nonnull String documentType, @Nonnull String filename) {
        return String.format("%s/%s_%s",
                documentType.toLowerCase(),
                LocalDateTime.now().format(DATE_FORMATTER),
                filename.replace(" ", "_"));
    }
}
