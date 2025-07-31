package com.hackathon.healsync.dto;

import java.time.LocalDate;
import java.util.List;

public class TreatmentPlanResponseDto {
    private Integer treatmentId;
    private Integer patientId;
    private Integer doctorId;
    private Integer diseaseId;
    private String status;
    private LocalDate startDate;
    private String notes;
    private List<TreatmentMedicineDto> medicines;

    // Getters and setters
    public Integer getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Integer treatmentId) {
        this.treatmentId = treatmentId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<TreatmentMedicineDto> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<TreatmentMedicineDto> medicines) {
        this.medicines = medicines;
    }

    public static class TreatmentMedicineDto {
        private Integer treatmentMedID;
        private String medicineName;
        private String dosage;
        private String timing;

        // Getters and setters
        public Integer getTreatmentMedID() {
            return treatmentMedID;
        }

        public void setTreatmentMedID(Integer treatmentMedID) {
            this.treatmentMedID = treatmentMedID;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        public String getTiming() {
            return timing;
        }

        public void setTiming(String timing) {
            this.timing = timing;
        }
    }
}
