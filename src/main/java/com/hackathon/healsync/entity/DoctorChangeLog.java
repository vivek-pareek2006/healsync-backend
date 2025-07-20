package com.hackathon.healsync.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer changeLogId;

    private Integer patientId;
    private Integer oldDoctorID;
    private Integer newDoctorId;
    @Column(columnDefinition = "TEXT")
    private String reason;
    private LocalDateTime changeDate;
}
