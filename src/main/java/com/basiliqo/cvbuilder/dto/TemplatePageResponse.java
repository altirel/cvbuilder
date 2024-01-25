package com.basiliqo.cvbuilder.dto;

import java.util.List;

public record TemplatePageResponse(List<TemplateMinimalResponse> templates,
                                   int page,
                                   int size,
                                   int maxPages) {
}
