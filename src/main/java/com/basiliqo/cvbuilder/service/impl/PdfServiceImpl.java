package com.basiliqo.cvbuilder.service.impl;

import com.basiliqo.cvbuilder.exception.FileProcessingException;
import com.basiliqo.cvbuilder.service.PdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service(PdfService.NAME)
public class PdfServiceImpl implements PdfService {

    @Override
    public List<String> extractParameters(MultipartFile file) throws IOException {
        List<String> parameters = new ArrayList<>();
        try (InputStream fileInputStream = file.getInputStream();
             PDDocument document = PDDocument.load(fileInputStream)) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String pdfText = pdfTextStripper.getText(document);

            int indexOfOpeningBracket = pdfText.indexOf("${");
            while (indexOfOpeningBracket != -1) {
                int indexOfClosingBracket = pdfText.indexOf("}", indexOfOpeningBracket);

                if (indexOfOpeningBracket < indexOfClosingBracket) {
                    String parameter = pdfText.substring(indexOfOpeningBracket + 2, indexOfClosingBracket);
                    parameters.add(parameter);
                } else {
                    throw new FileProcessingException("Error occurred while parsing parameters from template file. " +
                            "Closing bracket not found");
                }
                indexOfOpeningBracket = pdfText.indexOf("${", indexOfClosingBracket);
            }
        }

        return parameters;
    }
}
