package com.basiliqo.cvbuilder.service;

import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for processing document files.
 */
public interface DocumentFileService {

    String NAME = "cvb_DocumentFileService";

    /**
     * Saves file to file storage.
     *
     * @param file                file to save in file storage
     * @param documentType        file document type
     * @param documentContentType file document content type
     * @return response with saved file data
     */
    FileSaveResponse saveTemplateFile(MultipartFile file,
                                      DocumentType documentType,
                                      DocumentContentType documentContentType);
}
