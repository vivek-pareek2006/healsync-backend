package com.hackathon.healsync.dto;

import lombok.Data;

@Data
public class MedicineDto {
    private Integer medicineId;
    private String name;
    private String usageInfo;
    private String sideEffect;
}
