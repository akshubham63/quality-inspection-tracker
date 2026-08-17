package com.shopfloorquality.qualityinspection.controller;

import com.shopfloorquality.qualityinspection.dto.ApiResponse;
import com.shopfloorquality.qualityinspection.dto.InspectionResponse;
import com.shopfloorquality.qualityinspection.dto.SapWebhookRequest;
import com.shopfloorquality.qualityinspection.service.InspectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Expected JSON payload:
 * {
 *   "inspectionDate": "2024-01-15",
 *   "machineLineId": "LINE-001",
 *   "defectType": "WEAVE_DEFECT",
 *   "severity": "CRITICAL",
 *   "remarks": "Optional remarks",          // Optional: Additional notes
 *   "sapReferenceId": "SAP-12345"          // Optional: SAP reference ID (will be appended to remarks)
 * }
 */
@RestController
@RequestMapping("/api/sap-webhook")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SapWebhookController {

    private final InspectionService inspectionService;

    // receive inspection data from SAP system
    @PostMapping
    public ResponseEntity<ApiResponse<InspectionResponse>> receiveFromSap(
            @RequestBody SapWebhookRequest request) {
        log.info("POST /api/sap-webhook - Received webhook from SAP");
        
        // Validate required fields
        if (request.getMachineLineId() == null || request.getMachineLineId().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("MachineLineId is required"));
        }
        
        if (request.getDefectType() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("DefectType is required"));
        }
        
        if (request.getSeverity() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Severity is required"));
        }

        InspectionResponse response = inspectionService.createFromSapWebhook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inspection created from SAP webhook", response));
    }
}
