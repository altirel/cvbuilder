package com.basiliqo.cvbuilder.component;

import com.basiliqo.cvbuilder.enums.FileFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service to extract parameters from files.
 */
public interface TemplateParameterExtractor {

    /**
     * Extract parameters from files to collect all parameters (properties) that should be replaced in specified
     * template.
     *
     * @param file template file
     * @param fileFormat file format
     * @return list of parameters extracted from template file
     * @throws IOException if problem occurred while reading a template file
     */
    List<String> extractParameters(MultipartFile file, FileFormat fileFormat) throws IOException;
}
