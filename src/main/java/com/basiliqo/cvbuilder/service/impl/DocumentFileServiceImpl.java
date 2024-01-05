package com.basiliqo.cvbuilder.service.impl;

import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import com.basiliqo.cvbuilder.exception.FileSavingException;
import com.basiliqo.cvbuilder.mapper.FileDataMapper;
import com.basiliqo.cvbuilder.service.DocumentFileService;
import io.minio.MinioClient;
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
import java.util.Optional;

/**
 * Service to work with file storage.
 */
@Slf4j
@Service(DocumentFileService.NAME)
public class DocumentFileServiceImpl implements DocumentFileService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh:mm:ss");

    private final MinioClient minioClient;
    private final MinioServiceImpl minioBucketService;
    private final FileDataMapper fileDataMapper;

    public DocumentFileServiceImpl(MinioClient minioClient,
                                   MinioServiceImpl minioBucketService,
                                   FileDataMapper fileDataMapper) {
        this.minioClient = minioClient;
        this.minioBucketService = minioBucketService;
        this.fileDataMapper = fileDataMapper;
    }

    @Override
    public FileSaveResponse saveTemplateFile(MultipartFile file,
                                             DocumentType documentType,
                                             DocumentContentType documentContentType) {
        try {
            String bucketName = minioBucketService.getBucketNameByDocumentType(documentType);
            if (!minioBucketService.bucketExists(bucketName)) {
                minioBucketService.createBucket(bucketName);
            }

            return Optional.ofNullable(minioClient.putObject(createFileToSave(file, documentContentType, bucketName)))
                    .map(fileDataMapper::toFileSaveResponse)
                    .orElseThrow(() -> new FileSavingException("Error occurred while saving a file in MinIO"));
        } catch (ErrorResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException | InvalidResponseException | InvalidKeyException | InternalException |
                 InsufficientDataException e) {
            log.error("Error occurred while saving a file in MinIO", e);
            throw new FileSavingException("Error occurred while saving a file in MinIO", e);
        }
    }

    private PutObjectArgs createFileToSave(MultipartFile file,
                                           DocumentContentType documentContentType,
                                           String bucketName) throws IOException {
        if (file.getOriginalFilename() == null) {
            throw new FileSavingException("No filename provided");
        }

        return PutObjectArgs.builder()
                .bucket(bucketName)
                .object(generateFilepath(documentContentType.getId(), file.getOriginalFilename()))
                .stream(file.getInputStream(), file.getSize(), -1)
                .build();
    }

    @Nonnull
    private String generateFilepath(@Nonnull String documentType, @Nonnull String filename) {
        return String.format("%s/%s_%s",
                documentType.toLowerCase(),
                LocalDateTime.now().format(DATE_FORMATTER),
                filename.replace(" ", "_"));
    }
}
