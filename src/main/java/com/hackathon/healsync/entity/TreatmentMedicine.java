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
    
    // Explicit getters and setters for IDE compatibility
    public Integer getTreatmentMedID() { return treatmentMedID; }
    public void setTreatmentMedID(Integer treatmentMedID) { this.treatmentMedID = treatmentMedID; }
    
    public Integer getTreatmentID() { return treatmentID; }
    public void setTreatmentID(Integer treatmentID) { this.treatmentID = treatmentID; }
    
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    
    public String getTiming() { return timing; }
    public void setTiming(String timing) { this.timing = timing; }
    
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    
    public String getUsageInfo() { return usageInfo; }
    public void setUsageInfo(String usageInfo) { this.usageInfo = usageInfo; }
    
    public String getSideEffect() { return sideEffect; }
    public void setSideEffect(String sideEffect) { this.sideEffect = sideEffect; }
    
    public TreatmentPlan getTreatmentPlan() { return treatmentPlan; }
    public void setTreatmentPlan(TreatmentPlan treatmentPlan) { this.treatmentPlan = treatmentPlan; }
}
