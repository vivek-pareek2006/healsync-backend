package com.hackathon.healsync.dto;

import lombok.Data;
import java.util.List;

@Data
public class PatientDto {
    private Integer patientId;
    private String patientName;
    private Integer patientAge;
    private String gender;
    private String mobileNo;
    private String email;
    private String password;
    private List<Integer> treatmentPlanIds;
}
