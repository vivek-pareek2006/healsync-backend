package com.hackathon.healsync.repository;

import java.util.List;

import com.hackathon.healsync.entity.TreatmentMedicine;
import com.hackathon.healsync.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentMedicineRepository extends JpaRepository<TreatmentMedicine, Integer> {
    List<TreatmentMedicine> findByTreatmentPlan(TreatmentPlan treatmentPlan);
}
