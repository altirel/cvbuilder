package com.basiliqo.cvbuilder.service.impl;

import com.basiliqo.cvbuilder.component.FileFormatDetector;
import com.basiliqo.cvbuilder.component.TemplateParameterExtractor;
import com.basiliqo.cvbuilder.component.impl.FileFormatDetectorImpl;
import com.basiliqo.cvbuilder.component.impl.TemplateParameterExtractorImpl;
import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import com.basiliqo.cvbuilder.dto.TemplateDetailedResponse;
import com.basiliqo.cvbuilder.dto.TemplatePageResponse;
import com.basiliqo.cvbuilder.dto.TemplateSaveRequest;
import com.basiliqo.cvbuilder.entity.FileDetails;
import com.basiliqo.cvbuilder.entity.Template;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.basiliqo.cvbuilder.enums.DocumentType;
import com.basiliqo.cvbuilder.enums.FileFormat;
import com.basiliqo.cvbuilder.exception.FileProcessingException;
import com.basiliqo.cvbuilder.exception.TemplateNotFoundException;
import com.basiliqo.cvbuilder.exception.UnsupportedDocumentTypeException;
import com.basiliqo.cvbuilder.exception.WrongNameException;
import com.basiliqo.cvbuilder.mapper.TemplateMapper;
import com.basiliqo.cvbuilder.repository.TemplateRepository;
import com.basiliqo.cvbuilder.service.DocumentFileService;
import com.basiliqo.cvbuilder.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service(TemplateService.NAME)
public class TemplateServiceImpl implements TemplateService {

    private final TemplateMapper templateMapper;
    private final FileFormatDetector fileFormatDetector;
    private final TemplateRepository templateRepository;
    private final DocumentFileService documentFileService;
    private final TemplateParameterExtractor templateParameterExtractor;

    public TemplateServiceImpl(TemplateMapper templateMapper,
                               FileFormatDetectorImpl fileFormatDetector,
                               TemplateRepository templateRepository,
                               DocumentFileService documentFileService,
                               TemplateParameterExtractorImpl templateParameterExtractor) {
        this.templateMapper = templateMapper;
        this.fileFormatDetector = fileFormatDetector;
        this.templateRepository = templateRepository;
        this.documentFileService = documentFileService;
        this.templateParameterExtractor = templateParameterExtractor;
    }

    public TemplatePageResponse loadAllByType(@Nonnull DocumentContentType documentContentType, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("created").descending());
        Page<Template> templatesPage = templateRepository
                .findAllByDocumentContentType(documentContentType, pageRequest);
        return templateMapper.toTemplatePageResponse(templatesPage);
    }

    public TemplateDetailedResponse loadByIdAndType(String id, DocumentContentType documentContentType) {
        return templateRepository.findByIdAndDocumentContentType(id, documentContentType)
                .map(templateMapper::toDetailedResponse)
                .orElseThrow(() -> new TemplateNotFoundException("Template not found."));
    }

    public String save(TemplateSaveRequest request) {
        try {
            DocumentContentType type = Optional.of(request.type())
                    .map(DocumentContentType::fromId)
                    .orElseThrow(() -> new UnsupportedDocumentTypeException("Document type content is not supported"));

            String name = request.name();
            if (name == null) {
                throw new WrongNameException("Name is not specified");
            }
            if (templateRepository.existsByName(name)) {
                throw new WrongNameException("Name is already taken");
            }

            MultipartFile file = request.file();
            FileSaveResponse savedFile = documentFileService.saveTemplateFile(file, DocumentType.TEMPLATE, type);
            log.info("File \"{}\" was successfully saved", name);

            FileFormat fileFormat = fileFormatDetector.detectFileFormat(file.getOriginalFilename());
            List<String> parameters = templateParameterExtractor.extractParameters(file, fileFormat);
            FileDetails fileDetails = FileDetails.create(file.getOriginalFilename(), savedFile, fileFormat);

            Template template = templateRepository
                    .save(new Template(name, type, fileDetails, parameters, LocalDateTime.now()));
            return template.getId();
        } catch (IOException e) {
            log.error("Error occurred during file processing", e);
            throw new FileProcessingException("Error occurred during file processing");
        }
    }
}
