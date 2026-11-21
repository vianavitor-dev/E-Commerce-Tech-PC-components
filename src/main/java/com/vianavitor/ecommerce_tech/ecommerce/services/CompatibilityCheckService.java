package com.vianavitor.ecommerce_tech.ecommerce.services;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.PcCompatibilityCheckResultDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.PcComponentsReportDTO;
import com.vianavitor.ecommerce_tech.ecommerce.services.compatibility.CompatibilityCheck;
import com.vianavitor.ecommerce_tech.ecommerce.services.compatibility.CompatibilityCheckFactory;
import org.springframework.stereotype.Service;

@Service
public class CompatibilityCheckService {
    private final CompatibilityCheckFactory factory;

    public CompatibilityCheckService(CompatibilityCheckFactory factory) {
        this.factory = factory;
    }

    public PcCompatibilityCheckResultDTO checkPcComponentsCompatibility(PcComponentsDTO data) {
        // Keep report state local so concurrent requests cannot share results.
        var reportBuilder = PcComponentsReportDTO.builder();
        var extraInfoBuilder = new StringBuilder();
        for (CompatibilityCheck check : factory.createChecks(data)) {
            check.check(data, reportBuilder, extraInfoBuilder);
        }
        appendSkippedChecks(data, extraInfoBuilder);
        return new PcCompatibilityCheckResultDTO(reportBuilder.build(), extraInfoBuilder.toString());
    }

    private void appendSkippedChecks(PcComponentsDTO data, StringBuilder information) {
        if (data.getMotherboard() == null) {
            if (data.getCpu() != null) {
                information.append("CPU socket compatibility was not checked: no motherboard was supplied.\n");
            }
            if (data.getRams() != null && !data.getRams().isEmpty()) {
                information.append("RAM support, capacity and slots were not checked: no motherboard was supplied.\n");
            }
            if (data.getSsds() != null && !data.getSsds().isEmpty()) {
                information.append("SSD connectivity was not checked: no motherboard was supplied.\n");
            }
        } else if (data.getCpu() != null
                && (data.getCpu().getSocket() == null || data.getMotherboard().getSocket() == null)) {
            information.append("CPU socket compatibility was not checked: socket specifications are missing.\n");
        }
        if (data.getGpu() != null && data.getPsu() != null
                && (data.getGpu().getRecommendedPsuWatts() == null || data.getPsu().getWattageSupply() == null)) {
            information.append("GPU recommended PSU wattage was not checked: power specifications are missing.\n");
        }
        if (data.getPsu() == null && (data.getCpu() != null || data.getGpu() != null)) {
            information.append("Power compatibility was not checked: no PSU was supplied.\n");
        } else if (data.getPsu() != null && data.getCpu() != null
                && (data.getCpu().getTdpWatts() == null || data.getPsu().getWattageSupply() == null
                    || (data.getGpu() != null && data.getGpu().getPowerConsumption() == null))) {
            information.append("CPU/GPU power consumption was not checked: power specifications are missing.\n");
        }
    }
}
