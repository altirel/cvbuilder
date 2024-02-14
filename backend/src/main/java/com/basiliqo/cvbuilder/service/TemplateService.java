package com.basiliqo.cvbuilder.service;

import com.basiliqo.cvbuilder.dto.TemplateDetailedResponse;
import com.basiliqo.cvbuilder.dto.TemplatePageResponse;
import com.basiliqo.cvbuilder.dto.TemplateSaveRequest;
import com.basiliqo.cvbuilder.enums.DocumentContentType;

import javax.annotation.Nonnull;

/**
 * Service to work with Templates.
 */
public interface TemplateService {

    String NAME = "cvb_TemplateService";

    /**
     * Loads page number N of K templates with specified document content type where n is a number of the page and K is
     * number of templates on this page. Also Provides an information about overall number of pages.
     *
     * @param documentContentType document content type
     * @param page                page number
     * @param limit               specifies amount of entities that should be loaded
     * @return list of templates aggregated by provided pagination options and pagination info
     */
    TemplatePageResponse loadAllByType(@Nonnull DocumentContentType documentContentType, int page, int limit);

    /**
     * Loads single template's detailed information.
     *
     * @param id                  ID
     * @param documentContentType document content type
     * @return detailed information about template
     */
    TemplateDetailedResponse loadByIdAndType(String id, DocumentContentType documentContentType);

    /**
     * Saves new template file to storage and forms up new Template object storing it in database.
     *
     * @param request request with template file and details about content in it
     * @return ID of new template
     */
    String save(TemplateSaveRequest request);
}
