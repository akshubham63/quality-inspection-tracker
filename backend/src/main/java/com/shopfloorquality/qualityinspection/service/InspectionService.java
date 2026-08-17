package com.shopfloorquality.qualityinspection.service;

import com.shopfloorquality.qualityinspection.dto.*;
import com.shopfloorquality.qualityinspection.entity.Inspection;
import com.shopfloorquality.qualityinspection.enums.InspectionStatus;
import com.shopfloorquality.qualityinspection.enums.Severity;
import com.shopfloorquality.qualityinspection.exception.ResourceNotFoundException;
import com.shopfloorquality.qualityinspection.repository.InspectionRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InspectionService {

    private final InspectionRepository inspectionRepository;

    @Transactional
    public InspectionResponse createInspection(InspectionRequest request) {
        log.info("Creating new inspection for machine: {}", request.getMachineLineId());

        Inspection inspection = Inspection.builder()
                .inspectionDate(request.getInspectionDate())
                .machineLineId(request.getMachineLineId())
                .defectType(request.getDefectType())
                .severity(request.getSeverity())
                .remarks(request.getRemarks())
                .status(InspectionStatus.OPEN)
                .build();

        Inspection savedInspection = inspectionRepository.save(inspection);
        log.info("Inspection created with ID: {}", savedInspection.getId());

        return InspectionResponse.fromEntity(savedInspection);
    }

    public List<InspectionResponse> getAllInspections(
            Severity severity,
            InspectionStatus status,
            LocalDate startDate,
            LocalDate endDate,
            String sortBy,
            String sortDirection) {

        log.debug("Fetching inspections with filters - severity: {}, status: {}, startDate: {}, endDate: {}",
                severity, status, startDate, endDate);

        Specification<Inspection> spec = buildSpecification(severity, status, startDate, endDate);

        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "createdAt"
        );

        List<Inspection> inspections = inspectionRepository.findAll(spec, sort);

        return inspections.stream()
                .map(InspectionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public InspectionResponse getInspectionById(Long id) {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with id: " + id));
        return InspectionResponse.fromEntity(inspection);
    }

    @Transactional
    public InspectionResponse resolveInspection(Long id, ResolveRequest request) {
        log.info("Resolving inspection with ID: {}", id);

        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with id: " + id));

        if (inspection.getStatus() == InspectionStatus.RESOLVED) {
            throw new IllegalStateException("Inspection is already resolved");
        }

        inspection.setStatus(InspectionStatus.RESOLVED);
        inspection.setResolutionNote(request.getResolutionNote());
        inspection.setResolvedAt(LocalDateTime.now());

        Inspection savedInspection = inspectionRepository.save(inspection);
        log.info("Inspection {} resolved successfully", id);

        return InspectionResponse.fromEntity(savedInspection);
    }

    public SummaryResponse getSummary() {
        log.debug("Generating inspection summary");

        long criticalOpen = inspectionRepository.countByStatusAndSeverity(InspectionStatus.OPEN, Severity.CRITICAL);
        long criticalResolved = inspectionRepository.countByStatusAndSeverity(InspectionStatus.RESOLVED, Severity.CRITICAL);

        long majorOpen = inspectionRepository.countByStatusAndSeverity(InspectionStatus.OPEN, Severity.MAJOR);
        long majorResolved = inspectionRepository.countByStatusAndSeverity(InspectionStatus.RESOLVED, Severity.MAJOR);

        long minorOpen = inspectionRepository.countByStatusAndSeverity(InspectionStatus.OPEN, Severity.MINOR);
        long minorResolved = inspectionRepository.countByStatusAndSeverity(InspectionStatus.RESOLVED, Severity.MINOR);

        return SummaryResponse.builder()
                .critical(SummaryResponse.SeveritySummary.builder()
                        .open(criticalOpen)
                        .resolved(criticalResolved)
                        .total(criticalOpen + criticalResolved)
                        .build())
                .major(SummaryResponse.SeveritySummary.builder()
                        .open(majorOpen)
                        .resolved(majorResolved)
                        .total(majorOpen + majorResolved)
                        .build())
                .minor(SummaryResponse.SeveritySummary.builder()
                        .open(minorOpen)
                        .resolved(minorResolved)
                        .total(minorOpen + minorResolved)
                        .build())
                .totalOpen(criticalOpen + majorOpen + minorOpen)
                .totalResolved(criticalResolved + majorResolved + minorResolved)
                .total(criticalOpen + criticalResolved + majorOpen + majorResolved + minorOpen + minorResolved)
                .build();
    }

    @Transactional
    public InspectionResponse createFromSapWebhook(SapWebhookRequest request) {
        log.info("Creating inspection from SAP webhook, SAP Reference: {}", request.getSapReferenceId());

        String remarks = request.getRemarks();
        if (request.getSapReferenceId() != null && !request.getSapReferenceId().isEmpty()) {
            remarks = (remarks != null ? remarks + " | " : "") + "SAP Ref: " + request.getSapReferenceId();
        }

        Inspection inspection = Inspection.builder()
                .inspectionDate(request.getInspectionDate() != null ? request.getInspectionDate() : LocalDate.now())
                .machineLineId(request.getMachineLineId())
                .defectType(request.getDefectType())
                .severity(request.getSeverity())
                .remarks(remarks)
                .status(InspectionStatus.OPEN)
                .build();

        Inspection savedInspection = inspectionRepository.save(inspection);
        log.info("Inspection created from SAP webhook with ID: {}", savedInspection.getId());

        return InspectionResponse.fromEntity(savedInspection);
    }

    private Specification<Inspection> buildSpecification(
            Severity severity,
            InspectionStatus status,
            LocalDate startDate,
            LocalDate endDate) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (severity != null) {
                predicates.add(criteriaBuilder.equal(root.get("severity"), severity));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("inspectionDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("inspectionDate"), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
