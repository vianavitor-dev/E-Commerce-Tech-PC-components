package com.vianavitor.ecommerce_tech.ecommerce.services.compatibility;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.PcComponentsReportDTO.PcComponentsReportDTOBuilder;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Cpu;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Motherboard;

final class CpuCompatibilityCheck implements CompatibilityCheck {
    @Override
    public void check(PcComponentsDTO data, PcComponentsReportDTOBuilder reportBuilder, StringBuilder extraInfoBuilder) {
        Motherboard motherboard = data.getMotherboard();
        Cpu cpu = data.getCpu();

        final boolean isCompatible = cpu.getSocket().equals(motherboard.getSocket());
        String description = null;

        // Generates a simple description of why the sockets are incompatible
        if (!isCompatible) {
            int socketNumber = parseStrSocketToNumber(cpu.getSocket());
            int motherboardSocketNumber = parseStrSocketToNumber(motherboard.getSocket());
            boolean isCpuSocketOlder = socketNumber < motherboardSocketNumber;

            String aux = isCpuSocketOlder ? "a newer" : "an older";
            description = "this CPU is incompatible with the motherboard socket, try to choose a CPU from "+aux+" generation";
        }

        reportBuilder.cpu(new CompatibleReportDTO(isCompatible, cpu.getName(), description));
    }

    private int parseStrSocketToNumber(String str) {
        // Remove the first 2 letters, if it were an AMD socket then: [AM]5
        // else (an Intel socket): [LG]A1700
        StringBuilder sb = new StringBuilder(str.substring(2));

        // Remove the 'A' in case it is an Intel socket
        if (str.charAt(2) == 'A') {
            sb.deleteCharAt(0);
        }

        try {
            return Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}
