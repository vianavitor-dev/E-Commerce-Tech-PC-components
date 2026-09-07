package com.vianavitor.ecommerce_tech.dtos.response.aux;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record CompatibleReportDTO(
    @NotNull boolean isPcComponentCompatible,
    @NotNull String pcComponentName,
    @Nullable String reportText
) {}
