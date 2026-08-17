package com.shopfloorquality.qualityinspection.entity;

import com.shopfloorquality.qualityinspection.enums.DefectType;
import com.shopfloorquality.qualityinspection.enums.Severity;
import com.shopfloorquality.qualityinspection.enums.InspectionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inspection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Inspection date is required")
    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @NotBlank(message = "Machine/Line ID is required")
    @Column(name = "machine_line_id", nullable = false)
    private String machineLineId;

    @NotNull(message = "Defect type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "defect_type", nullable = false)
    private DefectType defectType;

    @NotNull(message = "Severity is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private InspectionStatus status = InspectionStatus.OPEN;

    @Column(name = "resolution_note", columnDefinition = "TEXT")
    private String resolutionNote;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
