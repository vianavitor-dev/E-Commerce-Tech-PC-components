package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.PcComponentsReportDTO.PcComponentsReportDTOBuilder;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Ram;

import java.util.ArrayList;
import java.util.List;

final class RamModuleCompatibilityCheck implements CompatibilityCheck {
    @Override
    public void check(PcComponentsDTO data, PcComponentsReportDTOBuilder reportBuilder, StringBuilder extraInfoBuilder) {
        List<CompatibleReportDTO> previous = reportBuilder.build().getRams();
        List<CompatibleReportDTO> reports = new ArrayList<>();
        for (int index = 0; index < data.getRams().size(); index++) {
            Ram ram = data.getRams().get(index);
            boolean generationMismatch = false;
            boolean formFactorMismatch = false;
            boolean compared = false;
            for (int otherIndex = 0; otherIndex < data.getRams().size(); otherIndex++) {
                if (index == otherIndex) {
                    continue;
                }
                Ram other = data.getRams().get(otherIndex);
                if (ram.getDdrGeneration() != null && other.getDdrGeneration() != null) {
                    compared = true;
                    generationMismatch |= !ram.getDdrGeneration().equals(other.getDdrGeneration());
                }
                if (ram.getFormFactor() != null && other.getFormFactor() != null) {
                    compared = true;
                    formFactorMismatch |= ram.getFormFactor() != other.getFormFactor();
                }
            }
            StringBuilder description = new StringBuilder();
            if (generationMismatch) {
                description.append("Selected RAM modules must use the same DDR generation.\n");
            }
            if (formFactorMismatch) {
                description.append("Selected RAM modules must use the same form factor.\n");
            }
            CompatibleReportDTO existing = previous == null ? null : previous.get(index);
            if (compared) {
                reports.add(CompatibilityReports.combine(existing, new CompatibleReportDTO(
                        !generationMismatch && !formFactorMismatch, ram.getName(), description.toString())));
            } else if (existing != null) {
                reports.add(existing);
            }
        }
        if (!reports.isEmpty()) {
            reportBuilder.rams(reports);
        }
    }
}
