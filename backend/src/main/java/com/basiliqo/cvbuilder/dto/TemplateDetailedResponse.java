package com.basiliqo.cvbuilder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record TemplateDetailedResponse(String id,
                                       String name,
                                       String type,
                                       @JsonProperty("file_format") String fileFormat,
                                       List<String> parameters,
                                       LocalDateTime created) {
}
