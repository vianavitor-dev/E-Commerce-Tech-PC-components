package com.vianavitor.ecommerce_tech.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vianavitor.ecommerce_tech.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.dtos.response.aux.PcComponentsReportDTO;
import lombok.*;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PcCompatibilityCheckResultDTO {
    private PcComponentsReportDTO componentsReport;
    private String extraInformation;

    @JsonProperty
    public boolean areAllComponentsCompatible() {
        return componentsReport.getCpu().isPcComponentCompatible()
                && componentsReport.getGpu().isPcComponentCompatible()
                && componentsReport.getRams().stream().anyMatch(CompatibleReportDTO::isPcComponentCompatible)
                && componentsReport.getSsds().stream().anyMatch(CompatibleReportDTO::isPcComponentCompatible);
    }

    @JsonProperty
    public int compatibleComponentsCount() {
        int compatibleCompCount = 0;

        if (componentsReport.getCpu() != null && componentsReport.getCpu().isPcComponentCompatible())
            compatibleCompCount ++;

        if (componentsReport.getGpu() != null && componentsReport.getGpu().isPcComponentCompatible())
            compatibleCompCount ++;

        compatibleCompCount += (int) Stream.concat(componentsReport.getSsds().stream(), componentsReport.getRams().stream())
                .filter(CompatibleReportDTO::isPcComponentCompatible)
                .count();

        return compatibleCompCount;
    }

    @JsonProperty
    public int incompatibleComponentsCount() {
        int compatibleCount = this.compatibleComponentsCount();

        int cpuCount = componentsReport.getCpu() != null ? 1 : 0;
        int gpuCount = componentsReport.getGpu() != null ? 1 : 0;
        int ssdsCount = componentsReport.getSsds().size();
        int ramsCount = componentsReport.getRams().size();

        int totalComponents = ssdsCount + ramsCount + cpuCount + gpuCount;

        return totalComponents - compatibleCount;
    }
}
