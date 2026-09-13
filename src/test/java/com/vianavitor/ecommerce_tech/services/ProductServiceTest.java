package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.dtos.response.PcCompatibilityCheckResultDTO;
import com.vianavitor.ecommerce_tech.models.*;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdProtocol;
import com.vianavitor.ecommerce_tech.repositories.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.Assert;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @MockitoBean
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Motherboard motherboard;
    private Psu psu;

    @BeforeEach
    void setUp() {
        motherboard = new Motherboard();
        // RAM
        motherboard.setRamSlots((byte) 4);
        motherboard.setDdrGeneration("DDR4");
        motherboard.setMaxRamCapacityGb((short) 128);
        // Memory
        motherboard.setM2Slots((byte) 1);
        motherboard.setSataSlots((byte) 1);
        // CPU
        motherboard.setSocket("LGA1700");

        psu = new Psu();
        psu.setWattageSupply((short) 500);
    }

    @Test
    public void checkPcComponentsCompatibilityTest__whenComponentsAreCompatible() {
        Cpu processor = new Cpu();
        processor.setName("Intel i5 13th U123f0");
        processor.setSocket("LGA1700");

        Ram ram1 = new Ram();
        ram1.setName("RAM-1");
        ram1.setDdrGeneration("DDR4");
        ram1.setCapacityGb((byte) 8);
        ram1.setModules((byte) 1);

        Ram ram2 = new Ram();
        ram2.setName("RAM-2");
        ram2.setDdrGeneration("DDR4");
        ram2.setCapacityGb((byte) 8);
        ram2.setModules((byte) 2);

        Ssd ssd1 = new Ssd();
        ssd1.setName("SSD-1");

        Ssd ssd2 = new Ssd();
        ssd2.setName("SSD-2");
        ssd2.setFormFactor(SsdFormFactor.M2);
        ssd2.setSsdInterface(SsdInterface.PCIE);
        ssd2.setProtocol(SsdProtocol.NVME);

        Gpu gpu = new Gpu();
        gpu.setName("GPU NVIDIA GTX 1030");
        gpu.setPowerConsumption((short) 240);
        gpu.setRecommendedPsuWatts((short) 500);

        List<Ram> ramList = List.of(ram1, ram2);
        List<Ssd> ssdList = List.of(ssd1, ssd2);

        PcComponentsDTO dto = PcComponentsDTO.builder(motherboard, psu)
                .cpu(processor).gpu(gpu)
                .rams(ramList).ssds(ssdList)
                .build();

        PcCompatibilityCheckResultDTO result = service.checkPcComponentsCompatibility(dto);

        Assert.isTrue(result.areAllComponentsCompatible(), "It was expected that all PC components were compatible");
        Assert.hasText(result.getComponentsReport().getCpu().pcComponentName(), "The CPU name shouldn't be null in this case");
        Assert.hasText(result.getComponentsReport().getRams().getLast().pcComponentName(), "The RAM name shouldn't be null in this case");
    }

    @Test
    public void checkPcComponentsCompatibilityTest__whenThereAreIncompatibility() {
        Ssd ssd1 = new Ssd();
        ssd1.setName("SSD-1");
        ssd1.setFormFactor(SsdFormFactor.M2);
        ssd1.setSsdInterface(SsdInterface.PCIE);
        ssd1.setProtocol(SsdProtocol.NVME);

        Ssd ssd2 = new Ssd();
        ssd2.setName("SSD-2");
        ssd2.setFormFactor(SsdFormFactor.M2);
        ssd2.setSsdInterface(SsdInterface.PCIE);
        ssd2.setProtocol(SsdProtocol.NVME);

        Ssd ssd3 = new Ssd();
        ssd3.setName("SSD-3");
        ssd3.setFormFactor(SsdFormFactor.M2);
        ssd3.setSsdInterface(SsdInterface.PCIE);
        ssd3.setProtocol(SsdProtocol.NVME);

        List<Ssd> ssdList = List.of(ssd1, ssd2, ssd3);

        PcComponentsDTO dto = PcComponentsDTO.builder(motherboard, psu)
                .ssds(ssdList)
                .build();
        PcCompatibilityCheckResultDTO result = service.checkPcComponentsCompatibility(dto);

        Assert.isTrue(!(result.areAllComponentsCompatible()), "It was expected that there were an incompatibility");
        Assertions.assertThat(result.incompatibleComponentsCount()).isEqualTo(2);
        Assert.hasText(result.getComponentsReport().getSsds().getLast().pcComponentName(), "The SSD name shouldn't be null in this case");
    }

}
