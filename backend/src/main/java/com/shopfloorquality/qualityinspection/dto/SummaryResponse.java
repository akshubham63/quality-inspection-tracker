package com.shopfloorquality.qualityinspection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryResponse {

    private SeveritySummary critical;
    private SeveritySummary major;
    private SeveritySummary minor;
    private long totalOpen;
    private long totalResolved;
    private long total;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SeveritySummary {
        private long open;
        private long resolved;
        private long total;
    }
}
