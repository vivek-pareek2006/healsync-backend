package com.hackathon.healsync.entity;

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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;

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
    private List<TreatmentMedicine> treatmentMedicines;
    private BigDecimal bill;
}
