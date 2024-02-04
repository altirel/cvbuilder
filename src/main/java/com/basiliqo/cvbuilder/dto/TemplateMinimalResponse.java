package com.basiliqo.cvbuilder.dto;

import java.time.LocalDateTime;

public record TemplateMinimalResponse(String id,
                                      String name,
                                      String type,
                                      LocalDateTime created) {
}
