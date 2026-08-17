package com.shopfloorquality.qualityinspection.enums;

import lombok.Getter;

@Getter
public enum DefectType {
    WEAVE_DEFECT("Weave Defect"),
    SHADE_VARIATION("Shade Variation"),
    HOLE_TEAR("Hole/Tear"),
    COUNT_DEVIATION("Count Deviation"),
    OTHER("Other");

    private final String displayName;

    DefectType(String displayName) {
        this.displayName = displayName;
    }

}
