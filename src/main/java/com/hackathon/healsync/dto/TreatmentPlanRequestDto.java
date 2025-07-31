package com.hackathon.healsync.dto;

import java.util.List;

public class TreatmentPlanRequestDto {
    private Integer doctorId;
    private Integer diseaseId;
    private String status;
    private String notes;
    private List<MedicineDto> medicines;

    // Getters and setters
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<MedicineDto> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<MedicineDto> medicines) {
        this.medicines = medicines;
    }

    public static class MedicineDto {
        private Integer medicineId;
        private String dosage;
        private String timing;

        // Getters and setters
        public Integer getMedicineId() {
            return medicineId;
        }

        public void setMedicineId(Integer medicineId) {
            this.medicineId = medicineId;
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