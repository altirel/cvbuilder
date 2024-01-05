package com.basiliqo.cvbuilder.entity;

import com.basiliqo.cvbuilder.enums.DocumentContentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "template")
public class Template {

    @Id
    private String id;

    private String name;

    @Field("document_type")
    @JsonProperty("document_type")
    private DocumentContentType documentContentType;

    @Field(name = "file")
    @JsonProperty("file")
    private FileDetails fileDetails;

    private List<String> parameters;

    private LocalDateTime created;

    public Template(String name,
                    DocumentContentType documentContentType,
                    FileDetails fileDetails,
                    List<String> parameters,
                    LocalDateTime created) {
        this.name = name;
        this.documentContentType = documentContentType;
        this.fileDetails = fileDetails;
        this.parameters = parameters;
        this.created = created;
    }
}
