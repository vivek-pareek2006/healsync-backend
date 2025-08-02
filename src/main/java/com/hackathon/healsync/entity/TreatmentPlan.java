package com.hackathon.healsync.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer treatmentId;

    private Integer diseaseId;
    private Integer doctorId;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    private String status;
    private LocalDate startDate;
    @Column(columnDefinition = "TEXT")
    private String notes;
    @OneToMany(mappedBy = "treatmentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TreatmentMedicine> treatmentMedicines = new ArrayList<>();

    public List<TreatmentMedicine> getTreatmentMedicines() {
        if (treatmentMedicines == null) {
            treatmentMedicines = new ArrayList<>();
        }
        return treatmentMedicines;
    }

    public void setTreatmentMedicines(List<TreatmentMedicine> treatmentMedicines) {
        this.treatmentMedicines = treatmentMedicines;
    }
}
