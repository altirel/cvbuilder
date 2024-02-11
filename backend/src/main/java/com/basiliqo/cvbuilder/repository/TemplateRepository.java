package com.basiliqo.cvbuilder.repository;

import com.basiliqo.cvbuilder.entity.Template;
import com.basiliqo.cvbuilder.enums.DocumentContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Template entity.
 */
@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {

    /**
     * Checks if template with provided name exists.
     *
     * @param name template name
     * @return true if template with provided name exists in database
     */
    boolean existsByName(String name);

    /**
     * Loads template by ID and document content type.
     *
     * @param id ID
     * @param documentContentType document content type
     * @return template if found with specified ID and document content type
     */
    Optional<Template> findByIdAndDocumentContentType(String id, DocumentContentType documentContentType);

    /**
     * Loads all templates by document content type and specified pagination option.
     *
     * @param documentContentType document content type
     * @param page pagination option
     * @return list of templates
     */
    Page<Template> findAllByDocumentContentType(DocumentContentType documentContentType, Pageable page);
}
