package com.shopfloorquality.qualityinspection.dto;

import com.shopfloorquality.qualityinspection.enums.DefectType;
import com.shopfloorquality.qualityinspection.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SapWebhookRequest {

    private LocalDate inspectionDate;
    private String machineLineId;
    private DefectType defectType;
    private Severity severity;
    private String remarks;
    private String sapReferenceId;
}
