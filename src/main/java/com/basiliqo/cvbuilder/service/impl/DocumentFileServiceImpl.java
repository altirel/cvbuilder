package com.basiliqo.cvbuilder.service.impl;

import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import com.basiliqo.cvbuilder.exception.FileSavingException;
import com.basiliqo.cvbuilder.mapper.FileDataMapper;
import com.basiliqo.cvbuilder.service.DocumentFileService;
import com.basiliqo.cvbuilder.service.MinioService;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Service to work with file storage.
 */
@Slf4j
@Service(DocumentFileService.NAME)
public class DocumentFileServiceImpl implements DocumentFileService {

    private final MinioService minioService;
    private final FileDataMapper fileDataMapper;

    public DocumentFileServiceImpl(MinioService minioService,
                                   FileDataMapper fileDataMapper) {
        this.minioService = minioService;
        this.fileDataMapper = fileDataMapper;
    }

    @Override
    public FileSaveResponse saveTemplateFile(MultipartFile file,
                                             DocumentType documentType,
                                             DocumentContentType documentContentType) {
        try {
            String bucketName = minioService.getBucketNameByDocumentType(documentType);
            if (bucketName == null) {
                throw new FileSavingException("Unable to save file. Unsupported document content type.");
            }
            if (!minioService.bucketExists(bucketName)) {
                minioService.createBucket(bucketName);
            }

            return Optional.ofNullable(minioService.saveFile(file, documentContentType, bucketName))
                    .map(fileDataMapper::toFileSaveResponse)
                    .orElseThrow(() -> new FileSavingException("Error occurred while saving a file in MinIO"));
        } catch (ErrorResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException | InvalidResponseException | InvalidKeyException | InternalException |
                 InsufficientDataException e) {
            log.error("Error occurred while saving a file in MinIO", e);
            throw new FileSavingException("Error occurred while saving a file in MinIO", e);
        }
    }
}
