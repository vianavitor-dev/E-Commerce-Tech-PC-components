package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.PcComponentsReportDTO.PcComponentsReportDTOBuilder;

/** Adds one component family's results to the report for the current request. */
public interface CompatibilityCheck {
    void check(PcComponentsDTO data, PcComponentsReportDTOBuilder reportBuilder, StringBuilder extraInfoBuilder);
}
