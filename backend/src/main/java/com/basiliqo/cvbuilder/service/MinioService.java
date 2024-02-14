package com.basiliqo.cvbuilder.service;

import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import io.minio.ObjectWriteResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Service for working with MinIO file storage.
 */
public interface MinioService {

    String NAME = "cvb_MinioService";

    /**
     * Detect bucket name from MinIO properties by provided document type.
     *
     * @param documentType document type
     * @return bucket name
     */
    String getBucketNameByDocumentType(DocumentType documentType);

    /**
     * Check if bucket exists in MinIO.
     *
     * @param bucketName bucket name
     * @return true - if bucket exists
     */
    boolean bucketExists(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
            InternalException;

    /**
     * Create new bucket with provided name.
     *
     * @param bucketName bucket name
     */
    void createBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
            InternalException;

    /**
     * Saves provided files to MinIO.
     *
     * @param file                file that should be saved to MinIO
     * @param documentContentType document content type
     * @param bucketName          bucket where file should be saved
     * @return object that contains data about saved file
     */
    ObjectWriteResponse saveFile(MultipartFile file,
                                 DocumentContentType documentContentType,
                                 String bucketName) throws IOException, ServerException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;
}
