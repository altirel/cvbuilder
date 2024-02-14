package com.basiliqo.cvbuilder.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service for processing files with .pdf format.
 */
public interface PdfService {

    String NAME = "cvb_PdfService";

    /**
     * Extract all parameters by following pattern '${PARAMETER_NAME}' and collect it to a list.
     *
     * @param file .pdf file which should be used to extract parameters
     * @return parameter names list
     * @throws IOException when something goes wrong with file reading
     */
    List<String> extractParameters(MultipartFile file) throws IOException;
}
