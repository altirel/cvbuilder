package com.basiliqo.cvbuilder.mapper;

import com.basiliqo.cvbuilder.dto.TemplateDetailedResponse;
import com.basiliqo.cvbuilder.dto.TemplateMinimalResponse;
import com.basiliqo.cvbuilder.dto.TemplatePageResponse;
import com.basiliqo.cvbuilder.entity.Template;
import org.springframework.data.domain.Page;

/**
 * Mapper for templates.
 */
public interface TemplateMapper {

    String NAME = "cvb_TemplateMapper";

    TemplateDetailedResponse toDetailedResponse(Template template);

    TemplateMinimalResponse toMinimalResponse(Template template);

    TemplatePageResponse toTemplatePageResponse(Page<Template> templatesPage);
}
