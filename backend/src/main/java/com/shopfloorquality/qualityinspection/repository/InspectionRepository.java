package com.shopfloorquality.qualityinspection.repository;

import com.shopfloorquality.qualityinspection.entity.Inspection;
import com.shopfloorquality.qualityinspection.enums.InspectionStatus;
import com.shopfloorquality.qualityinspection.enums.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long>, JpaSpecificationExecutor<Inspection> {

    List<Inspection> findByStatus(InspectionStatus status);

    List<Inspection> findBySeverity(Severity severity);

    List<Inspection> findByStatusAndSeverity(InspectionStatus status, Severity severity);

    List<Inspection> findByInspectionDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT i.severity, i.status, COUNT(i) FROM Inspection i GROUP BY i.severity, i.status")
    List<Object[]> getSummaryBySeverityAndStatus();

    long countByStatus(InspectionStatus status);

    long countBySeverity(Severity severity);

    long countByStatusAndSeverity(InspectionStatus status, Severity severity);
}
