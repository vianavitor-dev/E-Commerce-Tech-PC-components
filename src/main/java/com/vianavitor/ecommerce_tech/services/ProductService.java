package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.dtos.response.PcCompatibilityCheckResultDTO;
import com.vianavitor.ecommerce_tech.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.dtos.response.aux.PcComponentsReportDTO;
import com.vianavitor.ecommerce_tech.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.models.*;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

// TODO: create a Inventory System to manage the products
// TODO: implement PC components compatibility check
@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public Product findBySku(String sku) {
        return repository.findBySku(sku).
                orElseThrow(() -> new NotFoundResourceException(
                        "Not found any product with the provided SKU code"
                ));
    }

    public List<Product> findByName(String name) {
        return repository.findByNameContaining(name);
    }

    public List<Product> findByCategory(ProductCategory category) {
        return repository.findByCategory(category);
    }

    public List<Product> findByCategoryAndName(ProductCategory category, String name) {
        return repository.findByCategoryAndNameContaining(category, name);
    }

    private int parseStrSocketToNumber(String str) {
        // Remove the first 2 letters, if it were an AMD socket then: [AM]5
        // else (an Intel socket): [LG]A1700
        StringBuilder sb = new StringBuilder(str.substring(2));

        // Remove the 'A' in case it is an Intel socket
        if (str.charAt(2) == 'A') {
            sb.deleteCharAt(2);
        }

        try {
            return Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public PcCompatibilityCheckResultDTO checkPcComponentsCompatibility(PcComponentsDTO data) {
        PcComponentsReportDTO.PcComponentsReportDTOBuilder reportBuilder = PcComponentsReportDTO.builder();
        StringBuilder extraInfoBuilder = new StringBuilder();

        Motherboard motherboard = data.getMotherboard();
        Psu psu = data.getPsu();

        // 1. Checking CPU and motherboard compatibility
        if (data.getCpu() != null) {
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

        // 2. Checking GPU and PSU watts compatibility
        if (data.getGpu() != null) {
            Gpu gpu = data.getGpu();

            final boolean isWattageCompatible = psu.getWattageSupply() >= gpu.getRecommendedPsuWatts();
            String description = null;

            if (!isWattageCompatible) {
                description = "this GPU requeries more watts to work properly as recommended ("+gpu.getRecommendedPsuWatts()+" watts)";
            }

            reportBuilder.gpu(new CompatibleReportDTO(isWattageCompatible, gpu.getName(), description));
        }

        // 3. Checking RAMs and motherboard DDR generation compatibility
        if (data.getRams() != null && !data.getRams().isEmpty()) {
            int ramMemorySum = 0;
            List<CompatibleReportDTO> ramsReport = new ArrayList<>();

            for (Ram ram : data.getRams()) {
                boolean isCompatible = ram.getDdrGeneration().equals(motherboard.getDdrGeneration());
                String description = null;

                if (!isCompatible) {
                    int ramDdrGenerationNum = Integer.parseInt(ram.getDdrGeneration().substring(3));
                    int motherboardDdrGenerationNum = Integer.parseInt(motherboard.getDdrGeneration().substring(3));
                    boolean isRamDrrGenOlder = ramDdrGenerationNum < motherboardDdrGenerationNum;

                    String aux = isRamDrrGenOlder ? "older" : "newer";
                    description = "This RAM DDR generation is "+aux+" than the required ("+ motherboard.getDdrGeneration()+")";
                }

                ramsReport.add(new CompatibleReportDTO(isCompatible, ram.getName(), description));
                ramMemorySum += ram.getCapacityGb();
            }

            boolean isRamOverTheMaxCapacity = ramMemorySum > motherboard.getMaxRamCapacityGb();
            if (isRamOverTheMaxCapacity) {
                extraInfoBuilder.append("""
                            *The chosen RAMs capacity are exceeding the max capacity 
                            supported by your motherboard (%dGb). \n \n
                            """
                        .formatted(motherboard.getMaxRamCapacityGb())
                );
            }

            boolean isRamCountOverMaxRamSlots = data.getRams().size() > motherboard.getRamSlots();
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

        // 4. Checking SSDs compatibility with the motherboard
        if (data.getSsds() != null && !data.getSsds().isEmpty()) {
            int sataSlotsNeeded = 0;
            int m2SlotsNeeded = 0;

            boolean maxSataSlotsWasExceeded = false;
            boolean maxM2SlotsWasExceeded = false;

            List<CompatibleReportDTO> ssdsReport = new ArrayList<>();

            for (Ssd ssd : data.getSsds()) {
                SsdInterface ssdInterface = ssd.getSsdInterface();

                if (ssd.getFormFactor() == SsdFormFactor.M2) {
                    m2SlotsNeeded ++;
                }
                if (ssdInterface == SsdInterface.SATA) {
                    sataSlotsNeeded ++;
                }

                if (!(sataSlotsNeeded > motherboard.getSataSlots()) || !(m2SlotsNeeded > motherboard.getM2Slots())) {
                    maxSataSlotsWasExceeded = sataSlotsNeeded > motherboard.getSataSlots();
                    maxM2SlotsWasExceeded = m2SlotsNeeded > motherboard.getM2Slots();

                    continue;
                }

                String description = null;

                if (maxSataSlotsWasExceeded) {
                    description = "Your motherboard don't have enough SATA slots to connect this one";
                }
                if (maxM2SlotsWasExceeded) {
                    description = "Your motherboard don't have enough M.2 slots to connect this one";
                }

                ssdsReport.add(new CompatibleReportDTO(!(maxM2SlotsWasExceeded && maxSataSlotsWasExceeded), ssd.getName(), description));
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

        return new PcCompatibilityCheckResultDTO(reportBuilder.build(), extraInfoBuilder.toString());
    }
}
