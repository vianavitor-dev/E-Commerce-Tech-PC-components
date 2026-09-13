package com.vianavitor.ecommerce_tech.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vianavitor.ecommerce_tech.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.dtos.response.aux.PcComponentsReportDTO;
import com.vianavitor.ecommerce_tech.models.Cpu;
import lombok.*;

import java.util.Optional;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PcCompatibilityCheckResultDTO {
    private PcComponentsReportDTO componentsReport;
    private String extraInformation;

    @JsonProperty
    public boolean areAllComponentsCompatible() {
        boolean result = true;

        if (componentsReport.getCpu() != null) {
            result = result && componentsReport.getCpu().isPcComponentCompatible();
        }
        if (componentsReport.getGpu() != null) {
            result = result && componentsReport.getGpu().isPcComponentCompatible();
        }
        if (componentsReport.getRams() != null) {
            result = result && componentsReport.getRams().stream().allMatch(CompatibleReportDTO::isPcComponentCompatible);
        }
        if (componentsReport.getSsds() != null) {
            result = result && componentsReport.getSsds().stream().allMatch(CompatibleReportDTO::isPcComponentCompatible);
        }

        return result;
    }

    @JsonProperty
    public int compatibleComponentsCount() {
        int compatibleCompCount = 0;

        if (componentsReport.getCpu() != null && componentsReport.getCpu().isPcComponentCompatible())
            compatibleCompCount ++;

        if (componentsReport.getGpu() != null && componentsReport.getGpu().isPcComponentCompatible())
            compatibleCompCount ++;

        if (componentsReport.getSsds() != null) {
            compatibleCompCount += (int) componentsReport.getSsds().stream()
                    .filter(CompatibleReportDTO::isPcComponentCompatible)
                    .count();
        }
        if (componentsReport.getRams() != null) {
            compatibleCompCount += (int) componentsReport.getRams().stream()
                    .filter(CompatibleReportDTO::isPcComponentCompatible)
                    .count();
        }

        return compatibleCompCount;
    }

    @JsonProperty
    public int incompatibleComponentsCount() {
        int compatibleCount = this.compatibleComponentsCount();

        int cpuCount = componentsReport.getCpu() != null ? 1 : 0;
        int gpuCount = componentsReport.getGpu() != null ? 1 : 0;
        int ssdsCount = componentsReport.getSsds() != null ? componentsReport.getSsds().size() : 0;
        int ramsCount = componentsReport.getRams() != null ? componentsReport.getRams().size() : 0;

        int totalComponents = ssdsCount + ramsCount + cpuCount + gpuCount;
        return totalComponents - compatibleCount;
    }
}
