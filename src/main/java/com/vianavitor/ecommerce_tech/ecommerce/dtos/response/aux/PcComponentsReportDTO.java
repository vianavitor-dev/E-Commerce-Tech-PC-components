package com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.CompatibleReportDTO;

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
