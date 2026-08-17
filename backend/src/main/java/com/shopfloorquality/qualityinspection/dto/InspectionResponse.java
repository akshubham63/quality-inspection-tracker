package com.shopfloorquality.qualityinspection.dto;

import com.shopfloorquality.qualityinspection.entity.Inspection;
import com.shopfloorquality.qualityinspection.enums.DefectType;
import com.shopfloorquality.qualityinspection.enums.InspectionStatus;
import com.shopfloorquality.qualityinspection.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionResponse {

    private Long id;
    private LocalDate inspectionDate;
    private String machineLineId;
    private DefectType defectType;
    private String defectTypeDisplayName;
    private Severity severity;
    private String remarks;
    private InspectionStatus status;
    private String resolutionNote;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InspectionResponse fromEntity(Inspection inspection) {
        return InspectionResponse.builder()
                .id(inspection.getId())
                .inspectionDate(inspection.getInspectionDate())
                .machineLineId(inspection.getMachineLineId())
                .defectType(inspection.getDefectType())
                .defectTypeDisplayName(inspection.getDefectType().getDisplayName())
                .severity(inspection.getSeverity())
                .remarks(inspection.getRemarks())
                .status(inspection.getStatus())
                .resolutionNote(inspection.getResolutionNote())
                .resolvedAt(inspection.getResolvedAt())
                .createdAt(inspection.getCreatedAt())
                .updatedAt(inspection.getUpdatedAt())
                .build();
    }
}
