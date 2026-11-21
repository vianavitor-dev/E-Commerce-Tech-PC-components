package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompatibilityCheckFactory {
    // Select only checks whose required component pairs are present.
    public List<CompatibilityCheck> createChecks(PcComponentsDTO data) {
        List<CompatibilityCheck> checks = new ArrayList<>();
        if (data.getCpu() != null && data.getMotherboard() != null
                && data.getCpu().getSocket() != null && data.getMotherboard().getSocket() != null) {
            checks.add(new CpuCompatibilityCheck());
        }
        if (data.getGpu() != null && data.getPsu() != null
                && data.getGpu().getRecommendedPsuWatts() != null && data.getPsu().getWattageSupply() != null) {
            checks.add(new GpuCompatibilityCheck());
        }
        if (data.getMotherboard() != null && data.getRams() != null && !data.getRams().isEmpty()) {
            checks.add(new RamCompatibilityCheck());
        }
        if (data.getRams() != null && data.getRams().size() > 1) {
            checks.add(new RamModuleCompatibilityCheck());
        }
        if (data.getMotherboard() != null && data.getSsds() != null && !data.getSsds().isEmpty()) {
            checks.add(new SsdCompatibilityCheck());
        }
        if (data.getPsu() != null && data.getPsu().getWattageSupply() != null
                && data.getCpu() != null && data.getCpu().getTdpWatts() != null
                && (data.getGpu() == null || data.getGpu().getPowerConsumption() != null)) {
            checks.add(new PowerConsumptionCompatibilityCheck());
        }
        return checks;
    }
}
