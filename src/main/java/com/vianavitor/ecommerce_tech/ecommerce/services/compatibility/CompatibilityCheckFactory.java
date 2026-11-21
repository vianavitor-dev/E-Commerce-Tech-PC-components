package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompatibilityCheckFactory {
    // Checkers assume their components are present; retain the original report order.
    public List<CompatibilityCheck> createChecks(PcComponentsDTO data) {
        List<CompatibilityCheck> checks = new ArrayList<>();
        if (data.getCpu() != null) {
            checks.add(new CpuCompatibilityCheck());
        }
        if (data.getGpu() != null) {
            checks.add(new GpuCompatibilityCheck());
        }
        if (data.getRams() != null && !data.getRams().isEmpty()) {
            checks.add(new RamCompatibilityCheck());
        }
        if (data.getSsds() != null && !data.getSsds().isEmpty()) {
            checks.add(new SsdCompatibilityCheck());
        }
        return checks;
    }
}
