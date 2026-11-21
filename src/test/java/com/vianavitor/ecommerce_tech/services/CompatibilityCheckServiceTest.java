package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.PcCompatibilityCheckResultDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Cpu;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Gpu;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Motherboard;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Psu;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Ram;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Ssd;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.SsdProtocol;
import com.vianavitor.ecommerce_tech.ecommerce.services.CompatibilityCheckService;
import com.vianavitor.ecommerce_tech.ecommerce.services.compatibility.CompatibilityCheckFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.util.Assert;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompatibilityCheckServiceTest {
    private final CompatibilityCheckService service = new CompatibilityCheckService(new CompatibilityCheckFactory());

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

    @ParameterizedTest
    @CsvSource({"LGA1200,LGA1700,a newer", "LGA1700,LGA1200,an older", "AM4,AM5,a newer"})
    void preservesSocketMismatchDescriptions(String cpuSocket, String boardSocket, String generation) {
        Cpu cpu = new Cpu();
        cpu.setName("CPU");
        cpu.setSocket(cpuSocket);
        motherboard.setSocket(boardSocket);

        var result = service.checkPcComponentsCompatibility(
                PcComponentsDTO.builder(motherboard, psu).cpu(cpu).build());

        assertThat(result.getComponentsReport().getCpu().isPcComponentCompatible()).isFalse();
        assertThat(result.getComponentsReport().getCpu().reportText()).isEqualTo(
                "this CPU is incompatible with the motherboard socket, try to choose a CPU from "
                        + generation + " generation");
    }

    @ParameterizedTest
    @CsvSource({"499,true", "500,true", "501,false"})
    void preservesRecommendedPsuThreshold(short recommendedWatts, boolean compatible) {
        Gpu gpu = new Gpu();
        gpu.setName("GPU");
        gpu.setRecommendedPsuWatts(recommendedWatts);
        var result = service.checkPcComponentsCompatibility(
                PcComponentsDTO.builder(motherboard, psu).gpu(gpu).build());

        assertThat(result.areAllComponentsCompatible()).isEqualTo(compatible);
        assertThat(result.getComponentsReport().getGpu().reportText()).isEqualTo(compatible ? null :
                "this GPU requeries more watts to work properly as recommended (501 watts)");
    }

    @Test
    void preservesRamCapacityDdrAndSlotReports() {
        motherboard.setMaxRamCapacityGb((short) 8);
        motherboard.setRamSlots((byte) 1);
        Ram first = new Ram();
        first.setName("first");
        first.setCapacityGb((byte) 8);
        first.setDdrGeneration("DDR4");
        Ram second = new Ram();
        second.setName("second");
        second.setCapacityGb((byte) 8);
        second.setDdrGeneration("DDR3");

        var result = service.checkPcComponentsCompatibility(
                PcComponentsDTO.builder(motherboard, psu).rams(List.of(first, second)).build());

        assertThat(result.compatibleComponentsCount()).isEqualTo(1);
        assertThat(result.incompatibleComponentsCount()).isEqualTo(1);
        assertThat(result.getComponentsReport().getRams().getLast().reportText()).isEqualTo(
                "* This RAM DDR generation is older than the required (DDR4) \n"
                        + "* You have exceeded the max RAM capacity available by your motherboard, if you mistake please remove this one \n"
                        + "* You have selected more RAMs than your motherboard can support, if you mistake please remove this one");
        assertThat(result.getExtraInformation()).isEqualTo(
                "*The chosen RAMs capacity are exceeding the max capacity\n"
                        + "supported by your motherboard (8Gb). \n \n\n");
    }

    @Test
    void preservesSlotOnlyRamWarningWithoutChangingCompatibility() {
        motherboard.setRamSlots((byte) 0);
        Ram ram = new Ram();
        ram.setCapacityGb((byte) 8);
        ram.setDdrGeneration("DDR4");

        var result = service.checkPcComponentsCompatibility(
                PcComponentsDTO.builder(motherboard, psu).rams(List.of(ram)).build());

        // Preserve the existing rule: exceeding RAM slots reports a warning only.
        assertThat(result.areAllComponentsCompatible()).isTrue();
        assertThat(result.getComponentsReport().getRams().getFirst().reportText())
                .contains("more RAMs than your motherboard can support");
        assertThat(result.getExtraInformation()).isEmpty();
    }

    @Test
    void keepsSataAndM2CountsIndependentAndClearsReportsBetweenRequests() {
        Ssd sata = new Ssd();
        sata.setSsdInterface(SsdInterface.SATA);
        sata.setFormFactor(SsdFormFactor.INCH_2_5);
        Ssd m2 = new Ssd();
        m2.setSsdInterface(SsdInterface.PCIE);
        m2.setFormFactor(SsdFormFactor.M2);

        var result = service.checkPcComponentsCompatibility(PcComponentsDTO.builder(motherboard, psu)
                .ssds(List.of(sata, m2, sata, m2)).build());

        assertThat(result.compatibleComponentsCount()).isEqualTo(2);
        assertThat(result.incompatibleComponentsCount()).isEqualTo(2);
        assertThat(result.getComponentsReport().getSsds().get(2).reportText())
                .isEqualTo("Your motherboard don't have enough SATA slots to connect this one");
        assertThat(result.getComponentsReport().getSsds().get(3).reportText())
                .isEqualTo("Your motherboard don't have enough M.2 slots to connect this one");
        assertThat(result.getExtraInformation()).contains("SATA slots supported 1, selected 2", "M.2 slots supported 1, selected 2");

        var empty = service.checkPcComponentsCompatibility(PcComponentsDTO.builder(motherboard, psu)
                .rams(List.of()).ssds(List.of()).build());
        assertThat(empty.areAllComponentsCompatible()).isTrue();
        assertThat(empty.compatibleComponentsCount()).isZero();
        assertThat(empty.incompatibleComponentsCount()).isZero();
        assertThat(empty.getComponentsReport()).hasAllNullFieldsOrProperties();
        assertThat(empty.getExtraInformation()).isEmpty();
    }
}
