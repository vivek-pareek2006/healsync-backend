package com.hackathon.healsync.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private Integer doctorId;
    private String name;
    private String speaciality;
    private String email;
    private String mobileNo;
    private String bio;
    private String shift;
}
