    // ...existing code...
package com.hackathon.healsync.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hackathon.healsync.dto.AppointmentResponseDto;
import com.hackathon.healsync.entity.AppointmentStatus;
import com.hackathon.healsync.entity.Doctor;
import com.hackathon.healsync.entity.Patient;
import com.hackathon.healsync.repository.AppointmentStatusRepository;
import com.hackathon.healsync.repository.DoctorRepository;
import com.hackathon.healsync.repository.PatientRepository;
import com.hackathon.healsync.util.DoctorShift;

@Service
public class AppointmentService {
    private final DoctorRepository doctorRepository;
    private final AppointmentStatusRepository appointmentStatusRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(DoctorRepository doctorRepository,
                            AppointmentStatusRepository appointmentStatusRepository,
                            PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentStatusRepository = appointmentStatusRepository;
        this.patientRepository = patientRepository;
    }

    public boolean cancelAppointmentByDoctor(Integer appointmentId, Integer doctorId) {
        AppointmentStatus appointment = appointmentStatusRepository.findById(appointmentId).orElse(null);
        if (appointment != null && appointment.getDoctorId().equals(doctorId)) {
            appointment.setStatus("cancelled");
            appointmentStatusRepository.save(appointment);
            return true;
        }
        return false;
    }

    public Integer findAvailableDoctorId(String speaciality, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DoctorShift requiredShift = DoctorShift.fromTimeRange(startDateTime, endDateTime);
        List<Doctor> doctors = doctorRepository.findBySpeacialityAndShift(speaciality, requiredShift.name());
        for (var doctor : doctors) {
            var conflicts = appointmentStatusRepository.findConflictingAppointments(doctor.getDoctorId(), startDateTime, endDateTime);
            if (conflicts == null || conflicts.isEmpty()) {
                return doctor.getDoctorId();
            }
        }
        return null;
    }

    public AppointmentResponseDto bookAppointment(Integer patientId, String speaciality, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Integer doctorId = findAvailableDoctorId(speaciality, startDateTime, endDateTime);
        if (doctorId == null) {
            return null;
        }
        // Save appointment record in DB
        AppointmentStatus appointment = new AppointmentStatus();
        appointment.setDoctorId(doctorId);
        appointment.setPatientId(patientId);
        appointment.setStartTime(startDateTime);
        appointment.setEndTime(endDateTime);
        appointment.setStatus("booked");
        appointmentStatusRepository.save(appointment);

        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        Patient patient = patientRepository.findById(patientId).orElse(null);

        AppointmentResponseDto response = new AppointmentResponseDto();
        response.setAppointmentId(appointment.getScheduleId());
        if (doctor != null) {
            response.setDoctorId(doctor.getDoctorId());
            response.setDoctorName(doctor.getName());
        }
        if (patient != null) {
            response.setPatientId(patient.getPatientId());
            response.setPatientName(patient.getPatientName());
        }
        response.setDate(startDateTime.toLocalDate());
        response.setStartTime(startDateTime.toLocalTime());
        response.setEndTime(endDateTime.toLocalTime());
        response.setStatus(appointment.getStatus());
        return response;
    }

    public List<AppointmentStatus> listAppointmentsForPatient(Integer patientId) {
        return appointmentStatusRepository.findByPatientIdOrderByStartTimeDesc(patientId);
    }

    public List<AppointmentStatus> listAppointmentsForDoctor(Integer doctorId) {
        return appointmentStatusRepository.findByDoctorIdOrderByStartTimeDesc(doctorId);
    }

    public AppointmentStatus getAppointment(Integer appointmentId) {
        return appointmentStatusRepository.findById(appointmentId).orElse(null);
    }

    public AppointmentResponseDto rescheduleAppointment(Integer appointmentId, Integer requesterId, String requesterRole,
                                                        LocalDateTime newStart, LocalDateTime newEnd) {
        AppointmentStatus appt = appointmentStatusRepository.findById(appointmentId).orElse(null);
        if (appt == null) return null;
        boolean isDoctor = Objects.equals("DOCTOR", requesterRole);
        boolean isPatient = Objects.equals("PATIENT", requesterRole);
        if (!(isDoctor && Objects.equals(appt.getDoctorId(), requesterId)) &&
            !(isPatient && Objects.equals(appt.getPatientId(), requesterId))) {
            return null; // not allowed
        }
        Integer doctorId = appt.getDoctorId();
        // check conflicts for doctor
        var conflicts = appointmentStatusRepository.findConflictingAppointments(doctorId, newStart, newEnd);
        // remove current appt from conflicts if present
        conflicts.removeIf(a -> Objects.equals(a.getScheduleId(), appt.getScheduleId()));
        if (!conflicts.isEmpty()) {
            return null; // conflict
        }
        appt.setStartTime(newStart);
        appt.setEndTime(newEnd);
        appt.setStatus("rescheduled");
        appointmentStatusRepository.save(appt);

        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        Patient patient = patientRepository.findById(appt.getPatientId()).orElse(null);
        AppointmentResponseDto response = new AppointmentResponseDto();
        response.setAppointmentId(appt.getScheduleId());
        if (doctor != null) { response.setDoctorId(doctor.getDoctorId()); response.setDoctorName(doctor.getName()); }
        if (patient != null) { response.setPatientId(patient.getPatientId()); response.setPatientName(patient.getPatientName()); }
        response.setDate(newStart.toLocalDate());
        response.setStartTime(newStart.toLocalTime());
        response.setEndTime(newEnd.toLocalTime());
        response.setStatus(appt.getStatus());
        return response;
    }

    public boolean patientCancel(Integer appointmentId, Integer patientId) {
        AppointmentStatus appointment = appointmentStatusRepository.findById(appointmentId).orElse(null);
        if (appointment != null && Objects.equals(appointment.getPatientId(), patientId)) {
            appointment.setStatus( "cancelled_by_patient" );
            appointmentStatusRepository.save(appointment);
            return true;
        }
        return false;
    }

    public boolean addOrUpdateDoctorNotes(Integer appointmentId, Integer doctorId, String notes) {
        AppointmentStatus appointment = appointmentStatusRepository.findById(appointmentId).orElse(null);
        if (appointment != null && Objects.equals(appointment.getDoctorId(), doctorId)) {
            appointment.setDoctorNotes(notes);
            appointmentStatusRepository.save(appointment);
            return true;
        }
        return false;
    }

    public List<AppointmentStatus> filterAppointments(Integer patientId, Integer doctorId, String status, 
                                                     String startDate, String endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime start = null;
        LocalDateTime end = null;
        
        if (startDate != null && !startDate.isEmpty()) {
            start = LocalDate.parse(startDate).atStartOfDay();
        }
        if (endDate != null && !endDate.isEmpty()) {
            end = LocalDate.parse(endDate).atTime(23, 59, 59);
        }
        
        return appointmentStatusRepository.findByFilters(patientId, doctorId, status, start, end, pageable);
    }

    public Map<String, Object> checkDoctorAvailability(Integer doctorId, String startDateTime, String endDateTime) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            LocalDateTime start = LocalDateTime.parse(startDateTime);
            LocalDateTime end = LocalDateTime.parse(endDateTime);
            
            List<AppointmentStatus> conflicts = appointmentStatusRepository.findConflictingAppointments(doctorId, start, end);
            boolean isAvailable = conflicts.isEmpty();
            
            result.put("available", isAvailable);
            result.put("doctorId", doctorId);
            result.put("startTime", start);
            result.put("endTime", end);
            result.put("conflictingAppointments", conflicts.size());
            
        } catch (Exception e) {
            result.put("available", false);
            result.put("error", "Invalid date format");
        }
        
        return result;
    }

    public List<Map<String, Object>> getAvailableSlots(String specialty, String date, int durationMinutes) {
        List<Map<String, Object>> availableSlots = new ArrayList<>();
        
        try {
            LocalDate targetDate = LocalDate.parse(date);
            List<Doctor> doctors = doctorRepository.findBySpeaciality(specialty);
            
            for (Doctor doctor : doctors) {
                List<Map<String, Object>> doctorSlots = generateDoctorSlots(doctor, targetDate, durationMinutes);
                availableSlots.addAll(doctorSlots);
            }
            
        } catch (Exception e) {
            // Handle date parsing error
        }
        
        return availableSlots;
    }

    private List<Map<String, Object>> generateDoctorSlots(Doctor doctor, LocalDate date, int durationMinutes) {
        List<Map<String, Object>> slots = new ArrayList<>();
        
        // Generate slots based on doctor's shift (simplified example)
        LocalTime startTime = LocalTime.of(9, 0); // Default start time
        LocalTime endTime = LocalTime.of(17, 0);   // Default end time
        
        LocalDateTime slotStart = date.atTime(startTime);
        LocalDateTime slotEnd = slotStart.plusMinutes(durationMinutes);
        
        while (slotEnd.toLocalTime().isBefore(endTime) || slotEnd.toLocalTime().equals(endTime)) {
            List<AppointmentStatus> conflicts = appointmentStatusRepository.findConflictingAppointments(
                doctor.getDoctorId(), slotStart, slotEnd);
            
            if (conflicts.isEmpty()) {
                Map<String, Object> slot = new HashMap<>();
                slot.put("doctorId", doctor.getDoctorId());
                slot.put("doctorName", doctor.getName());
                slot.put("startTime", slotStart);
                slot.put("endTime", slotEnd);
                slot.put("available", true);
                slots.add(slot);
            }
            
            slotStart = slotStart.plusMinutes(durationMinutes);
            slotEnd = slotStart.plusMinutes(durationMinutes);
        }
        
        return slots;
    }

    public List<AppointmentStatus> getUpcomingAppointments(Integer patientId, Integer doctorId, int daysAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(daysAhead);
        
        if (patientId != null) {
            return appointmentStatusRepository.findUpcomingByPatient(patientId, now, futureDate);
        } else if (doctorId != null) {
            return appointmentStatusRepository.findUpcomingByDoctor(doctorId, now, futureDate);
        } else {
            return appointmentStatusRepository.findAllUpcoming(now, futureDate);
        }
    }

    public Map<String, Object> bulkCancelAppointments(List<Integer> appointmentIds, Integer requesterId, 
                                                     String requesterRole, String reason) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Integer appointmentId : appointmentIds) {
            try {
                boolean success = false;
                if ("DOCTOR".equals(requesterRole)) {
                    success = cancelAppointmentByDoctor(appointmentId, requesterId);
                } else if ("PATIENT".equals(requesterRole)) {
                    success = patientCancel(appointmentId, requesterId);
                }
                
                if (success) {
                    successCount++;
                    // Add cancellation reason if provided
                    if (reason != null && !reason.isEmpty()) {
                        AppointmentStatus appointment = appointmentStatusRepository.findById(appointmentId).orElse(null);
                        if (appointment != null) {
                            appointment.setDoctorNotes(reason);
                            appointmentStatusRepository.save(appointment);
                        }
                    }
                } else {
                    failureCount++;
                    errors.add("Failed to cancel appointment " + appointmentId);
                }
            } catch (Exception e) {
                failureCount++;
                errors.add("Error canceling appointment " + appointmentId + ": " + e.getMessage());
            }
        }
        
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("totalProcessed", appointmentIds.size());
        result.put("errors", errors);
        
        return result;
    }

    public Map<String, Object> getAppointmentStatistics(Integer doctorId, String startDate, String endDate) {
        Map<String, Object> stats = new HashMap<>();
        
        LocalDateTime start = startDate != null ? LocalDate.parse(startDate).atStartOfDay() : 
                             LocalDateTime.now().minusDays(30);
        LocalDateTime end = endDate != null ? LocalDate.parse(endDate).atTime(23, 59, 59) : 
                           LocalDateTime.now();
        
        List<AppointmentStatus> appointments;
        if (doctorId != null) {
            appointments = appointmentStatusRepository.findByDoctorIdAndDateRange(doctorId, start, end);
        } else {
            appointments = appointmentStatusRepository.findByDateRange(start, end);
        }
        
        long totalAppointments = appointments.size();
        long bookedCount = appointments.stream().filter(a -> "booked".equals(a.getStatus())).count();
        long cancelledCount = appointments.stream().filter(a -> a.getStatus().contains("cancelled")).count();
        long completedCount = appointments.stream().filter(a -> "completed".equals(a.getStatus())).count();
        long confirmedCount = appointments.stream().filter(a -> "confirmed".equals(a.getStatus())).count();
        
        stats.put("totalAppointments", totalAppointments);
        stats.put("bookedAppointments", bookedCount);
        stats.put("cancelledAppointments", cancelledCount);
        stats.put("completedAppointments", completedCount);
        stats.put("confirmedAppointments", confirmedCount);
        stats.put("startDate", start);
        stats.put("endDate", end);
        
        return stats;
    }

    public boolean confirmAppointment(Integer appointmentId, Integer doctorId) {
        AppointmentStatus appointment = appointmentStatusRepository.findById(appointmentId).orElse(null);
        if (appointment != null && Objects.equals(appointment.getDoctorId(), doctorId)) {
            appointment.setStatus("confirmed");
            appointmentStatusRepository.save(appointment);
            return true;
        }
        return false;
    }

    public boolean completeAppointment(Integer appointmentId, Integer doctorId, String notes, String prescription) {
        AppointmentStatus appointment = appointmentStatusRepository.findById(appointmentId).orElse(null);
        if (appointment != null && Objects.equals(appointment.getDoctorId(), doctorId)) {
            appointment.setStatus("completed");
            if (notes != null && !notes.isEmpty()) {
                appointment.setDoctorNotes(notes);
            }
            if (prescription != null && !prescription.isEmpty()) {
                appointment.setPrescription(prescription);
            }
            appointmentStatusRepository.save(appointment);
            return true;
        }
        return false;
    }

    public List<AppointmentStatus> searchAppointments(String query, Integer userId, String userRole, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        if ("PATIENT".equals(userRole) && userId != null) {
            return appointmentStatusRepository.searchPatientAppointments(userId, query, pageable);
        } else if ("DOCTOR".equals(userRole) && userId != null) {
            return appointmentStatusRepository.searchDoctorAppointments(userId, query, pageable);
        } else {
            return appointmentStatusRepository.searchAllAppointments(query, pageable);
        }
    }
}
