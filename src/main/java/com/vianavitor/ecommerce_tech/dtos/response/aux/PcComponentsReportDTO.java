package com.vianavitor.ecommerce_tech.dtos.response.aux;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PcComponentsReportDTO {
    private CompatibleReportDTO cpu;
    private CompatibleReportDTO gpu;
    private List<CompatibleReportDTO> rams;
    private List<CompatibleReportDTO> ssds;
}
