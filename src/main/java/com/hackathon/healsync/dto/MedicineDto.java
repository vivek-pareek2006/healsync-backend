package com.hackathon.healsync.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineDto {
    private Integer medicineId;
    private String name;
    private String usage;
    private String sideEffect;
}
