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
import com.hackathon.healsync.dto.TreatmentPlanListDto;
import com.hackathon.healsync.dto.TreatmentMedicineDto;
import com.hackathon.healsync.entity.Medicine;
import com.hackathon.healsync.entity.Patient;
import com.hackathon.healsync.entity.Doctor;
import com.hackathon.healsync.entity.Disease;
import com.hackathon.healsync.entity.TreatmentMedicine;
import com.hackathon.healsync.entity.TreatmentPlan;
import com.hackathon.healsync.repository.MedicineRepository;
import com.hackathon.healsync.repository.PatientRepository;
import com.hackathon.healsync.repository.DoctorRepository;
import com.hackathon.healsync.repository.DiseaseRepository;
import com.hackathon.healsync.repository.TreatmentMedicineRepository;
import com.hackathon.healsync.repository.TreatmentPlanRepository;

@Service
public class TreatmentPlanService {

    private final TreatmentPlanRepository treatmentPlanRepository;
    private final TreatmentMedicineRepository treatmentMedicineRepository;
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;
    private final DoctorRepository doctorRepository;
    private final DiseaseRepository diseaseRepository;

    @Autowired
    public TreatmentPlanService(
        TreatmentPlanRepository treatmentPlanRepository,
        TreatmentMedicineRepository treatmentMedicineRepository,
        PatientRepository patientRepository,
        MedicineRepository medicineRepository,
        DoctorRepository doctorRepository,
        DiseaseRepository diseaseRepository
    ) {
        this.treatmentPlanRepository = treatmentPlanRepository;
        this.treatmentMedicineRepository = treatmentMedicineRepository;
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
        this.doctorRepository = doctorRepository;
        this.diseaseRepository = diseaseRepository;
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

    /**
     * Get all treatment plans created by a specific doctor
     */
    public List<TreatmentPlanListDto> getTreatmentPlansByDoctor(Integer doctorId) {
        List<TreatmentPlan> plans = treatmentPlanRepository.findByDoctorIdOrderByStartDateDesc(doctorId);
        return plans.stream()
                .map(this::mapToListDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all treatment plans for a specific patient with different doctors
     */
    public List<TreatmentPlanListDto> getTreatmentPlansByPatient(Integer patientId) {
        List<TreatmentPlan> plans = treatmentPlanRepository.findByPatientIdWithDoctorInfo(patientId);
        return plans.stream()
                .map(this::mapToListDto)
                .collect(Collectors.toList());
    }

    /**
     * Map TreatmentPlan to TreatmentPlanListDto with doctor and patient information
     */
    private TreatmentPlanListDto mapToListDto(TreatmentPlan plan) {
        TreatmentPlanListDto dto = new TreatmentPlanListDto();
        dto.setTreatmentId(plan.getTreatmentId());
        dto.setDiseaseId(plan.getDiseaseId());
        dto.setDoctorId(plan.getDoctorId());
        dto.setPatientId(plan.getPatient().getPatientId());
        dto.setPatientName(plan.getPatient().getPatientName());
        dto.setStatus(plan.getStatus());
        dto.setStartDate(plan.getStartDate());
        dto.setNotes(plan.getNotes());

        // Get doctor information
        if (plan.getDoctorId() != null) {
            Optional<Doctor> doctorOpt = doctorRepository.findById(plan.getDoctorId());
            if (doctorOpt.isPresent()) {
                Doctor doctor = doctorOpt.get();
                dto.setDoctorName(doctor.getName());
                dto.setDoctorSpecialty(doctor.getSpecialty());
            }
        }

        // Get disease information
        if (plan.getDiseaseId() != null) {
            Optional<Disease> diseaseOpt = diseaseRepository.findById(plan.getDiseaseId());
            if (diseaseOpt.isPresent()) {
                Disease disease = diseaseOpt.get();
                dto.setDiseaseName(disease.getName());
            }
        }

        // Map treatment medicines
        List<TreatmentMedicineDto> medicineDtos = plan.getTreatmentMedicines().stream()
                .map(this::mapTreatmentMedicineToDto)
                .collect(Collectors.toList());
        dto.setMedicines(medicineDtos);

        return dto;
    }

    /**
     * Map TreatmentMedicine to TreatmentMedicineDto
     */
    private TreatmentMedicineDto mapTreatmentMedicineToDto(TreatmentMedicine medicine) {
        TreatmentMedicineDto dto = new TreatmentMedicineDto();
        dto.setTreatmentMedID(medicine.getTreatmentMedID());
        dto.setTreatmentID(medicine.getTreatmentID());
        dto.setDosage(medicine.getDosage());
        dto.setTiming(medicine.getTiming());
        dto.setMedicineName(medicine.getMedicineName());
        dto.setUsageInfo(medicine.getUsageInfo());
        dto.setSideEffect(medicine.getSideEffect());
        if (medicine.getTreatmentPlan() != null) {
            dto.setTreatmentPlanId(medicine.getTreatmentPlan().getTreatmentId());
        }
        return dto;
    }
}
