package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.PcComponentsReportDTO.PcComponentsReportDTOBuilder;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Gpu;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Psu;

final class GpuCompatibilityCheck implements CompatibilityCheck {
    @Override
    public void check(PcComponentsDTO data, PcComponentsReportDTOBuilder reportBuilder, StringBuilder extraInfoBuilder) {
        Psu psu = data.getPsu();
        Gpu gpu = data.getGpu();

        final boolean isWattageCompatible = psu.getWattageSupply() >= gpu.getRecommendedPsuWatts();
        String description = null;

        if (!isWattageCompatible) {
            description = "this GPU requeries more watts to work properly as recommended ("+gpu.getRecommendedPsuWatts()+" watts)";
        }

        reportBuilder.gpu(new CompatibleReportDTO(isWattageCompatible, gpu.getName(), description));
    }
}
