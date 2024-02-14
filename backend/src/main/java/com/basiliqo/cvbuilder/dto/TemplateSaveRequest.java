package com.basiliqo.cvbuilder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record TemplateSaveRequest(@NotBlank(message = "Template name is required")
                                  String name,
                                  @NotBlank(message = "Template type is required")
                                  String type,
                                  @NotNull(message = "Template file is required")
                                  MultipartFile file) {
}
