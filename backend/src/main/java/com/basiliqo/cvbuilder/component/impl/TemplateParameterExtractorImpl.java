package com.basiliqo.cvbuilder.component.impl;

import com.basiliqo.cvbuilder.component.TemplateParameterExtractor;
import com.basiliqo.cvbuilder.enums.FileFormat;
import com.basiliqo.cvbuilder.exception.UnsupportedFileFormatException;
import com.basiliqo.cvbuilder.service.DocxService;
import com.basiliqo.cvbuilder.service.PdfService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class TemplateParameterExtractorImpl implements TemplateParameterExtractor {

    private final PdfService pdfService;
    private final DocxService docxService;

    public TemplateParameterExtractorImpl(PdfService pdfService,
                                          DocxService docxService) {
        this.pdfService = pdfService;
        this.docxService = docxService;
    }

    @Override
    public List<String> extractParameters(MultipartFile file, FileFormat fileFormat) throws IOException {
        if (fileFormat == FileFormat.DOCX) {
            return docxService.extractParameters(file);
        }
        if (fileFormat == FileFormat.PDF) {
            return pdfService.extractParameters(file);
        }
        throw new UnsupportedFileFormatException("File format is not supported");
    }
}
