package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.PcComponentsReportDTO.PcComponentsReportDTOBuilder;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Motherboard;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Ssd;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.SsdInterface;

import java.util.ArrayList;
import java.util.List;

final class SsdCompatibilityCheck implements CompatibilityCheck {
    @Override
    public void check(PcComponentsDTO data, PcComponentsReportDTOBuilder reportBuilder, StringBuilder extraInfoBuilder) {
        Motherboard motherboard = data.getMotherboard();
        int sataSlotsNeeded = 0;
        int m2SlotsNeeded = 0;

        boolean maxSataSlotsWasExceeded = false;
        boolean maxM2SlotsWasExceeded = false;

        List<CompatibleReportDTO> ssdsReport = new ArrayList<>();

        for (Ssd ssd : data.getSsds()) {
            SsdInterface ssdInterface = ssd.getSsdInterface();

            // Count form factor and interface separately; an M.2 SATA drive uses both limits.
            if (ssd.getFormFactor() == SsdFormFactor.M2) {
                m2SlotsNeeded ++;
            }
            if (ssdInterface == SsdInterface.SATA) {
                sataSlotsNeeded ++;
            }

            if (!(maxSataSlotsWasExceeded) || !(maxM2SlotsWasExceeded)) {
                maxSataSlotsWasExceeded = sataSlotsNeeded > motherboard.getSataSlots();
                maxM2SlotsWasExceeded = m2SlotsNeeded > motherboard.getM2Slots();
            }

            String description = null;

            if (maxSataSlotsWasExceeded && ssd.getSsdInterface().equals(SsdInterface.SATA)) {
                description = "Your motherboard don't have enough SATA slots to connect this one";
            }
            if (maxM2SlotsWasExceeded && ssd.getSsdInterface().equals(SsdInterface.PCIE)) {
                description = "Your motherboard don't have enough M.2 slots to connect this one";
            }

            boolean isCurrentSsdIncompatible = (maxM2SlotsWasExceeded && ssd.getSsdInterface().equals(SsdInterface.PCIE))
                    || (maxSataSlotsWasExceeded && ssd.getSsdInterface().equals(SsdInterface.SATA));

            ssdsReport.add(new CompatibleReportDTO(!(isCurrentSsdIncompatible), ssd.getName(), description));
        }

        String template = """
                    *You have exceed the %s slots available on the provided motherboard,
                    please check it if it was a mistake.

                    %s slots supported %d, selected %d. \n \n
                    """;

        if (maxSataSlotsWasExceeded) {
            extraInfoBuilder.append(template.formatted("SATA", "SATA", motherboard.getSataSlots(), sataSlotsNeeded)
            );
        }
        if (maxM2SlotsWasExceeded) {
            extraInfoBuilder.append(template.formatted("M.2", "M.2", motherboard.getM2Slots(), m2SlotsNeeded)
            );
        }

        reportBuilder.ssds(ssdsReport);
    }
}
