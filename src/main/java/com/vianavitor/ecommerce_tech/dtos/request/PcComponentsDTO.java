package com.vianavitor.ecommerce_tech.dtos.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.vianavitor.ecommerce_tech.models.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PcComponentsDTO {
    private Motherboard motherboard;
    private Cpu cpu;
    private Gpu gpu;
    private Psu psu;
    private List<Ram> rams;
    private List<Ssd> ssds;

    @Builder
    private PcComponentsDTO(Motherboard motherboard, Cpu cpu, Gpu gpu, Psu psu, List<Ram> rams, List<Ssd> ssds) {
        this.motherboard = motherboard;
        this.cpu = cpu;
        this.gpu = gpu;
        this.psu = psu;
        this.rams = rams;
        this.ssds = ssds;
    }

    public static PcComponentsDTOBuilder builder(Motherboard motherboard, Psu psu) {
        if (motherboard == null || psu == null) {
            throw new IllegalArgumentException("the fields motherboard and psu (power supply unit) are both required");
        }

        return new PcComponentsDTOBuilder().motherboard(motherboard).psu(psu);
    }
}
