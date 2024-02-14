package com.basiliqo.cvbuilder.entity;

import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@org.springframework.data.mongodb.core.mapping.Document(collection = "document")
public class Document {

    @Id
    private String id;

    private String name;

    @Field("document_type")
    @JsonAlias("document_type")
    private DocumentContentType documentContentType;

    @Field("file")
    @JsonProperty("document_type")
    private FileDetails fileDetails;

    private Map<String, String> parameters;

    private LocalDateTime created;

    public Document(String name,
                    DocumentContentType documentContentType,
                    FileDetails fileDetails,
                    Map<String, String> parameters,
                    LocalDateTime created) {
        this.name = name;
        this.documentContentType = documentContentType;
        this.fileDetails = fileDetails;
        this.parameters = parameters;
        this.created = created;
    }
}
