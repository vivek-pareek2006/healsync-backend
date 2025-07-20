package com.hackathon.healsync.dto;

import lombok.Data;

@Data
public class DiseaseDto {
    private Integer diseaseId;
    private String name;
    private String description;
    private String precaution;
}
