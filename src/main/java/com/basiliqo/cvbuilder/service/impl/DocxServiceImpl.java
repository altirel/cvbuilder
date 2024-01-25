package com.basiliqo.cvbuilder.service.impl;

import com.basiliqo.cvbuilder.exception.FileProcessingException;
import com.basiliqo.cvbuilder.service.DocxService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service(DocxService.NAME)
public class DocxServiceImpl implements DocxService {

    @Override
    public List<String> extractParameters(MultipartFile file) throws IOException {
        List<String> parameters = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream)) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String docText = paragraph.getText();
                int indexOfOpeningBracket = docText.indexOf("${");
                while (indexOfOpeningBracket != -1) {
                    int indexOfClosingBracket = docText.indexOf("}", indexOfOpeningBracket);

                    if (indexOfOpeningBracket < indexOfClosingBracket) {
                        String parameter = docText.substring(indexOfOpeningBracket + 2, indexOfClosingBracket);
                        parameters.add(parameter);
                    } else {
                        throw new FileProcessingException("Error occurred while parsing parameters from template " +
                                "file. Closing bracket not found");
                    }
                    indexOfOpeningBracket = docText.indexOf("${", indexOfClosingBracket);
                }
            }
        }
        return parameters;
    }
}
