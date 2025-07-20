package com.hackathon.healsync.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer treatmentMedID;

    private Integer treatmentID;
    private String dosage;
    private String timing;
    private String medicineName;
    @Column(name = "usage_info", columnDefinition = "TEXT")
    private String usageInfo;
    @Column(columnDefinition = "TEXT")
    private String sideEffect;
    @ManyToOne
    @JoinColumn(name = "treatment_id")
    private TreatmentPlan treatmentPlan;
}
