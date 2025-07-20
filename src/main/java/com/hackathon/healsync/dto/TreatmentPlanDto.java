package com.hackathon.healsync.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TreatmentPlanDto {
    private Integer treatmentId;
    private Integer diseaseId;
    private Integer doctorId;
    private Integer patientId;
    private String status;
    private LocalDate startDate;
    private String notes;
    private List<Integer> treatmentMedicineIds;
    private BigDecimal bill;
}
