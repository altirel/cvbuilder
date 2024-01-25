package com.basiliqo.cvbuilder.component.impl;

import com.basiliqo.cvbuilder.component.FileFormatDetector;
import com.basiliqo.cvbuilder.enums.FileFormat;
import com.basiliqo.cvbuilder.exception.UnsupportedFileFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component(FileFormatDetector.NAME)
public class FileFormatDetectorImpl implements FileFormatDetector {

    @Override
    public FileFormat detectFileFormat(String fileName) {
        if (fileName == null) {
            log.error("File format is not supported");
            throw new UnsupportedFileFormatException("File format is not supported");
        }
        if (fileName.endsWith(".pdf")) {
            return FileFormat.PDF;
        }
        if (fileName.endsWith(".docx")) {
            return FileFormat.DOCX;
        }
        log.error("File format is not supported");
        throw new UnsupportedFileFormatException("File format is not supported");
    }
}
