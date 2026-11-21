package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.PcComponentsReportDTO.PcComponentsReportDTOBuilder;

final class PowerConsumptionCompatibilityCheck implements CompatibilityCheck {
    @Override
    public void check(PcComponentsDTO data, PcComponentsReportDTOBuilder reportBuilder, StringBuilder extraInfoBuilder) {
        // TDP and GPU draw give a partial power estimate, not a full-system PSU recommendation.
        int watts = data.getCpu().getTdpWatts()
                + (data.getGpu() == null ? 0 : data.getGpu().getPowerConsumption());
        boolean compatible = data.getPsu().getWattageSupply() >= watts;
        String description = compatible ? null
                : "Selected CPU/GPU power demand (" + watts + " watts) exceeds the PSU capacity ("
                    + data.getPsu().getWattageSupply() + " watts).";
        var current = new CompatibleReportDTO(compatible, data.getCpu().getName(), description);
        reportBuilder.cpu(CompatibilityReports.combine(reportBuilder.build().getCpu(), current));
        if (data.getGpu() != null) {
            reportBuilder.gpu(CompatibilityReports.combine(reportBuilder.build().getGpu(),
                    new CompatibleReportDTO(compatible, data.getGpu().getName(), description)));
        }
        extraInfoBuilder.append("Power check uses CPU TDP and the selected GPU's power consumption only; "
                + "other components and PSU headroom are not included.\n");
    }
}
