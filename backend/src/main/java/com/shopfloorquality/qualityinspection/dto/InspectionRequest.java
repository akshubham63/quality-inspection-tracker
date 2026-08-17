package com.shopfloorquality.qualityinspection.dto;

import com.shopfloorquality.qualityinspection.enums.DefectType;
import com.shopfloorquality.qualityinspection.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionRequest {

    @NotNull(message = "Inspection date is required")
    private LocalDate inspectionDate;

    @NotBlank(message = "Machine/Line ID is required")
    private String machineLineId;

    @NotNull(message = "Defect type is required")
    private DefectType defectType;

    @NotNull(message = "Severity is required")
    private Severity severity;

    private String remarks;
}
