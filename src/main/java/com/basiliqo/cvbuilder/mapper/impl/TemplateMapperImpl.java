package com.basiliqo.cvbuilder.mapper.impl;

import com.basiliqo.cvbuilder.dto.TemplateDetailedResponse;
import com.basiliqo.cvbuilder.dto.TemplateMinimalResponse;
import com.basiliqo.cvbuilder.dto.TemplatePageResponse;
import com.basiliqo.cvbuilder.entity.Template;
import com.basiliqo.cvbuilder.mapper.TemplateMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component(TemplateMapper.NAME)
public class TemplateMapperImpl implements TemplateMapper {

    @Override
    public TemplateDetailedResponse toDetailedResponse(Template template) {
        return new TemplateDetailedResponse(template.getId(),
                template.getName(),
                template.getDocumentContentType().getId(),
                template.getFileDetails().getFileFormat().getId(),
                template.getParameters(),
                template.getCreated());
    }

    @Override
    public TemplateMinimalResponse toMinimalResponse(Template template) {
        return new TemplateMinimalResponse(template.getName(),
                template.getDocumentContentType().getId(),
                template.getCreated());
    }

    @Override
    public TemplatePageResponse toTemplatePageResponse(Page<Template> templatesPage) {
        List<TemplateMinimalResponse> templates = templatesPage.getContent().stream()
                .map(this::toMinimalResponse)
                .collect(Collectors.toList());
        return new TemplatePageResponse(templates,
                templatesPage.getNumber(),
                templatesPage.getSize(),
                templatesPage.getTotalPages());
    }
}
