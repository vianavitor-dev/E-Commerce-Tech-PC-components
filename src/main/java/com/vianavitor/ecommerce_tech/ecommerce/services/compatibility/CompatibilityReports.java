package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.CompatibleReportDTO;

final class CompatibilityReports {
    private CompatibilityReports() {}

    // A component can participate in multiple checks; keep every failure and explanation.
    static CompatibleReportDTO combine(CompatibleReportDTO previous, CompatibleReportDTO current) {
        if (previous == null) {
            return current;
        }
        String description = previous.reportText();
        if (current.reportText() != null && !current.reportText().isEmpty()) {
            description = description == null || description.isEmpty()
                    ? current.reportText() : description + "\n" + current.reportText();
        }
        return new CompatibleReportDTO(previous.isPcComponentCompatible() && current.isPcComponentCompatible(),
                current.pcComponentName(), description);
    }
}
