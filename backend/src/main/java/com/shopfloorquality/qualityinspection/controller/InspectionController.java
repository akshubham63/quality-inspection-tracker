package com.shopfloorquality.qualityinspection.controller;

import com.shopfloorquality.qualityinspection.dto.*;
import com.shopfloorquality.qualityinspection.enums.InspectionStatus;
import com.shopfloorquality.qualityinspection.enums.Severity;
import com.shopfloorquality.qualityinspection.service.InspectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inspections")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class InspectionController {

    private final InspectionService inspectionService;

    // create new inspection
    @PostMapping
    public ResponseEntity<ApiResponse<InspectionResponse>> createInspection(
            @Valid @RequestBody InspectionRequest request) {
        log.info("POST /api/inspections - Creating new inspection");
        InspectionResponse response = inspectionService.createInspection(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inspection created successfully", response));
    }

    // get all inspections with filters
    @GetMapping
    public ResponseEntity<ApiResponse<List<InspectionResponse>>> getAllInspections(
            @RequestParam(required = false) Severity severity,
            @RequestParam(required = false) InspectionStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        
        log.info("GET /api/inspections - Fetching inspections with filters");
        List<InspectionResponse> inspections = inspectionService.getAllInspections(
                severity, status, startDate, endDate, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(inspections));
    }

    // get inspection by inspection id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InspectionResponse>> getInspectionById(@PathVariable Long id) {
        log.info("GET /api/inspections/{} - Fetching inspection", id);
        InspectionResponse response = inspectionService.getInspectionById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // resolve inspection item with resolution note
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<InspectionResponse>> resolveInspection(
            @PathVariable Long id,
            @Valid @RequestBody ResolveRequest request) {
        log.info("PATCH /api/inspections/{}/resolve - Resolving inspection", id);
        InspectionResponse response = inspectionService.resolveInspection(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inspection resolved successfully", response));
    }

    // get summary of inspections
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<SummaryResponse>> getSummary() {
        log.info("GET /api/inspections/summary - Fetching summary");
        SummaryResponse summary = inspectionService.getSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}
