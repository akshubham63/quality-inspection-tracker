package com.shopfloorquality.qualityinspection.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResolveRequest {

    @NotBlank(message = "Resolution note is required")
    private String resolutionNote;
}
