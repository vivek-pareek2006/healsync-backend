package com.hackathon.healsync.dto;

import lombok.Data;

@Data
public class TreatmentMedicineDto {
    private Integer treatmentMedID;
    private Integer treatmentID;
    private String dosage;
    private String timing;
    private String medicineName;
    private String usageInfo;
    private String sideEffect;
    private Integer treatmentPlanId;
}
