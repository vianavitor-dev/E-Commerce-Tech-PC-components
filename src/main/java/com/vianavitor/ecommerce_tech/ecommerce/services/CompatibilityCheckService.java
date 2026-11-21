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
        return new PcCompatibilityCheckResultDTO(reportBuilder.build(), extraInfoBuilder.toString());
    }
}
