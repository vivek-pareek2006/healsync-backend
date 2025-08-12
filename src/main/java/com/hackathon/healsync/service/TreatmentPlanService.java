package com.hackathon.healsync.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hackathon.healsync.dto.TreatmentPlanRequestDto;
import com.hackathon.healsync.dto.TreatmentPlanResponseDto;
import com.hackathon.healsync.entity.Medicine;
import com.hackathon.healsync.entity.Patient;
import com.hackathon.healsync.entity.TreatmentMedicine;
import com.hackathon.healsync.entity.TreatmentPlan;
import com.hackathon.healsync.repository.MedicineRepository;
import com.hackathon.healsync.repository.PatientRepository;
import com.hackathon.healsync.repository.TreatmentMedicineRepository;
import com.hackathon.healsync.repository.TreatmentPlanRepository;

@Service
public class TreatmentPlanService {

    private final TreatmentPlanRepository treatmentPlanRepository;
    private final TreatmentMedicineRepository treatmentMedicineRepository;
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;

    @Autowired
    public TreatmentPlanService(
        TreatmentPlanRepository treatmentPlanRepository,
        TreatmentMedicineRepository treatmentMedicineRepository,
        PatientRepository patientRepository,
        MedicineRepository medicineRepository
    ) {
        this.treatmentPlanRepository = treatmentPlanRepository;
        this.treatmentMedicineRepository = treatmentMedicineRepository;
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
    }

    @Transactional
    public TreatmentPlanResponseDto createTreatmentPlan(Integer patientId, TreatmentPlanRequestDto dto) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        TreatmentPlan plan = new TreatmentPlan();
        plan.setPatient(patient);
        plan.setDoctorId(dto.getDoctorId());
        plan.setDiseaseId(dto.getDiseaseId());
        plan.setStatus(dto.getStatus());
        plan.setNotes(dto.getNotes());
        plan.setStartDate(java.time.LocalDate.now()); // Set start date to current date
        TreatmentPlan savedPlan = treatmentPlanRepository.save(plan);

        if (dto.getMedicines() != null) {
            for (TreatmentPlanRequestDto.MedicineDto med : dto.getMedicines()) {
                TreatmentMedicine tm = new TreatmentMedicine();
                tm.setTreatmentPlan(savedPlan);
                tm.setTreatmentID(savedPlan.getTreatmentId()); // Set the treatment ID
                tm.setDosage(med.getDosage());
                tm.setTiming(med.getTiming());

                // fetch medicine name if ID provided
                String medName = medicineRepository.findById(med.getMedicineId())
                    .map(Medicine::getName)
                    .orElse(null);
                tm.setMedicineName(medName);

                treatmentMedicineRepository.save(tm);
            }
        }

        // Fetch the medicines directly and set them on the saved plan
        List<TreatmentMedicine> savedMedicines = treatmentMedicineRepository.findByTreatmentPlan(savedPlan);
        savedPlan.setTreatmentMedicines(savedMedicines);
        
        return mapToResponseDto(savedPlan);
    }

    private TreatmentPlanResponseDto mapToResponseDto(TreatmentPlan plan) {
        TreatmentPlanResponseDto responseDto = new TreatmentPlanResponseDto();
        responseDto.setTreatmentId(plan.getTreatmentId());
        responseDto.setPatientId(plan.getPatient().getPatientId());
        responseDto.setDoctorId(plan.getDoctorId());
        responseDto.setDiseaseId(plan.getDiseaseId());
        responseDto.setStatus(plan.getStatus());
        responseDto.setStartDate(plan.getStartDate());
        responseDto.setNotes(plan.getNotes());

        // ✅ Fixed: Always set medicines list, never null
        List<TreatmentMedicine> medicines = plan.getTreatmentMedicines();
        if (medicines != null && !medicines.isEmpty()) {
            responseDto.setMedicines(
                medicines.stream()
                    .map(this::mapMedicineToDto)
                    .collect(Collectors.toList())
            );
        } else {
            // ✅ Set empty list instead of leaving it null
            responseDto.setMedicines(new ArrayList<>());
        }

        return responseDto;
    }

    private TreatmentPlanResponseDto.TreatmentMedicineDto mapMedicineToDto(TreatmentMedicine medicine) {
        TreatmentPlanResponseDto.TreatmentMedicineDto dto = new TreatmentPlanResponseDto.TreatmentMedicineDto();
        dto.setTreatmentMedID(medicine.getTreatmentMedID());
        dto.setMedicineName(medicine.getMedicineName());
        dto.setDosage(medicine.getDosage());
        dto.setTiming(medicine.getTiming());
        return dto;
    }

    /**
     * Get all treatment plans (for doctor dashboard)
     */
    public List<TreatmentPlanResponseDto> getAllTreatmentPlans() {
        List<TreatmentPlan> allPlans = treatmentPlanRepository.findAll();
        return allPlans.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get treatment plan by ID
     */
    public TreatmentPlanResponseDto getTreatmentPlanById(Integer treatmentId) {
        Optional<TreatmentPlan> planOpt = treatmentPlanRepository.findById(treatmentId);
        if (planOpt.isEmpty()) {
            return null;
        }
        
        TreatmentPlan plan = planOpt.get();
        // Ensure medicines are loaded
        List<TreatmentMedicine> medicines = treatmentMedicineRepository.findByTreatmentPlan(plan);
        plan.setTreatmentMedicines(medicines);
        
        return mapToResponseDto(plan);
    }
}
