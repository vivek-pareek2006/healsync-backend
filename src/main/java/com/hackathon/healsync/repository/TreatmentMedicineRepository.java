package com.hackathon.healsync.repository;

import com.hackathon.healsync.entity.TreatmentMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentMedicineRepository extends JpaRepository<TreatmentMedicine, Integer> {
}
