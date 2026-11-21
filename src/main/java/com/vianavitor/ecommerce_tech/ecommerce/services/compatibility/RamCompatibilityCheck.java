package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.PcComponentsReportDTO.PcComponentsReportDTOBuilder;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Motherboard;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Ram;

import java.util.ArrayList;
import java.util.List;

final class RamCompatibilityCheck implements CompatibilityCheck {
    @Override
    public void check(PcComponentsDTO data, PcComponentsReportDTOBuilder reportBuilder, StringBuilder extraInfoBuilder) {
        Motherboard motherboard = data.getMotherboard();
        int ramMemorySum = 0;
        List<CompatibleReportDTO> ramsReport = new ArrayList<>();

        boolean isRamOverTheMaxCapacity = false;
        boolean isRamCountOverMaxRamSlots = ramMemorySum > motherboard.getMaxRamCapacityGb();

        for (Ram ram : data.getRams()) {
            // Capacity is cumulative: reports follow the RAMs' request order.
            ramMemorySum += ram.getCapacityGb();

            boolean isRamDdrGenCompatible = ram.getDdrGeneration().equals(motherboard.getDdrGeneration());
            isRamOverTheMaxCapacity = ramMemorySum > motherboard.getMaxRamCapacityGb();

            StringBuilder descriptionBuilder = new StringBuilder();

            if (!isRamDdrGenCompatible) {
                int ramDdrGenerationNum = Integer.parseInt(ram.getDdrGeneration().substring(3));
                int motherboardDdrGenerationNum = Integer.parseInt(motherboard.getDdrGeneration().substring(3));
                boolean isRamDrrGenOlder = ramDdrGenerationNum < motherboardDdrGenerationNum;

                String aux = isRamDrrGenOlder ? "older" : "newer";
                descriptionBuilder.append(
                        "* This RAM DDR generation is %s than the required (%s) \n"
                                .formatted(aux, motherboard.getDdrGeneration())
                );
            }

            if (isRamOverTheMaxCapacity) {
                descriptionBuilder.append(
                        "* You have exceeded the max RAM capacity available by your motherboard, " +
                                "if you mistake please remove this one \n"
                );
            }

            boolean isCurrentRamExceedingMaxRamSlots = (1 + ramsReport.size()) > motherboard.getRamSlots();
            if (isCurrentRamExceedingMaxRamSlots) {
                descriptionBuilder.append(
                        "* You have selected more RAMs than your motherboard can support, " +
                                "if you mistake please remove this one"
                );
            }

            // Preserve the existing rule: slot overflow adds a warning only.
            boolean isCompatible = isRamDdrGenCompatible && !(isRamOverTheMaxCapacity);
            ramsReport.add(new CompatibleReportDTO(isCompatible, ram.getName(), descriptionBuilder.toString()));
        }

        if (isRamOverTheMaxCapacity) {
            extraInfoBuilder.append("""
                        *The chosen RAMs capacity are exceeding the max capacity
                        supported by your motherboard (%dGb). \n \n
                        """
                    .formatted(motherboard.getMaxRamCapacityGb())
            );
        }

        if (isRamCountOverMaxRamSlots) {
            extraInfoBuilder.append("""
                        *You have chosen more RAMs than your motherboard can supports.
                        Your motherboard has %d RAMs slots, you have selected %d.

                        If it was a mistake, please remove one of them. \n \n
                        """
                    .formatted(motherboard.getRamSlots(), data.getRams().size())
            );
        }

        reportBuilder.rams(ramsReport);
    }
}
