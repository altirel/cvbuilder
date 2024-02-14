package com.basiliqo.cvbuilder.component;

import com.basiliqo.cvbuilder.enums.FileFormat;
import com.basiliqo.cvbuilder.exception.UnsupportedFileFormatException;

/**
 * Component to detect file format.
 */
public interface FileFormatDetector {

    String NAME = "cvb_FileFormatDetector";

    /**
     * Detects a file format by provided file name.
     *
     * @param fileName file name
     * @return file format
     * @throws UnsupportedFileFormatException if file format is not supported by system
     */
    FileFormat detectFileFormat(String fileName);
}
