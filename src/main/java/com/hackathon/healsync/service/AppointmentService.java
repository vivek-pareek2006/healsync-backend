package com.hackathon.healsync.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hackathon.healsync.dto.AppointmentResponseDto;
import com.hackathon.healsync.dto.AppointmentWithDoctorDto;
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

    public Integer findAvailableDoctorId(String specialty, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DoctorShift requiredShift = DoctorShift.fromTimeRange(startDateTime, endDateTime);
        List<Doctor> doctors = doctorRepository.findBySpecialtyAndShift(specialty, requiredShift.name());
        for (var doctor : doctors) {
            var conflicts = appointmentStatusRepository.findConflictingAppointments(doctor.getDoctorId(), startDateTime, endDateTime);
            if (conflicts == null || conflicts.isEmpty()) {
                return doctor.getDoctorId();
            }
        }
        return null;
    }

    public AppointmentResponseDto bookAppointment(Integer patientId, String specialty, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Integer doctorId = findAvailableDoctorId(specialty, startDateTime, endDateTime);
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

    public AppointmentResponseDto bookAppointmentWithDoctor(Integer patientId, Integer doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Check if doctor exists
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return null; // Doctor not found
        }

        // Check if doctor is available at the requested time
        if (!isDoctorAvailable(doctorId, startDateTime, endDateTime)) {
            return null; // Doctor not available
        }

        // Save appointment record in DB
        AppointmentStatus appointment = new AppointmentStatus();
        appointment.setDoctorId(doctorId);
        appointment.setPatientId(patientId);
        appointment.setStartTime(startDateTime);
        appointment.setEndTime(endDateTime);
        appointment.setStatus("booked");
        appointmentStatusRepository.save(appointment);

        Patient patient = patientRepository.findById(patientId).orElse(null);

        AppointmentResponseDto response = new AppointmentResponseDto();
        response.setAppointmentId(appointment.getScheduleId());
        response.setDoctorId(doctor.getDoctorId());
        response.setDoctorName(doctor.getName());
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

    private boolean isDoctorAvailable(Integer doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Check for conflicting appointments
        List<AppointmentStatus> conflictingAppointments = appointmentStatusRepository
            .findConflictingAppointments(doctorId, startDateTime, endDateTime);
        
        return conflictingAppointments.isEmpty();
    }

    public List<AppointmentStatus> listAppointmentsForPatient(Integer patientId) {
        return appointmentStatusRepository.findByPatientIdOrderByStartTimeDesc(patientId);
    }

    /**
     * Enhanced method that returns appointments with complete doctor and patient information
     * This fixes the frontend issue where doctor information is missing from appointments
     */
    public List<AppointmentWithDoctorDto> listAppointmentsForPatientWithDoctorInfo(Integer patientId) {
        List<AppointmentStatus> appointments = appointmentStatusRepository.findByPatientIdOrderByStartTimeDesc(patientId);
        List<AppointmentWithDoctorDto> enhancedAppointments = new ArrayList<>();
        
        for (AppointmentStatus appt : appointments) {
            // Fetch doctor information
            Doctor doctor = doctorRepository.findById(appt.getDoctorId()).orElse(null);
            Patient patient = patientRepository.findById(appt.getPatientId()).orElse(null);
            
            AppointmentWithDoctorDto dto = new AppointmentWithDoctorDto();
            
            // Set appointment fields
            dto.setScheduleId(appt.getScheduleId());
            dto.setId(appt.getScheduleId()); // Fix: Use scheduleId as appointment ID
            dto.setDoctorId(appt.getDoctorId());
            dto.setPatientId(appt.getPatientId());
            dto.setStartTime(appt.getStartTime());
            dto.setEndTime(appt.getEndTime());
            dto.setStatus(appt.getStatus());
            dto.setDoctorNotes(appt.getDoctorNotes());
            dto.setPrescription(appt.getPrescription());
            
            // Set doctor information (fixes missing doctor data)
            if (doctor != null) {
                dto.setDoctorName(doctor.getName());
                dto.setDoctorSpecialty(doctor.getSpeaciality()); // Map to corrected spelling
                dto.setDoctorEmail(doctor.getEmail());
                dto.setDoctorMobileNo(doctor.getMobileNo());
                dto.setDoctorBio(doctor.getBio());
                dto.setDoctorShift(doctor.getShift());
            }
            
            // Set patient information
            if (patient != null) {
                dto.setPatientName(patient.getPatientName());
                dto.setPatientEmail(patient.getEmail());
                dto.setPatientMobileNo(patient.getMobileNo());
                dto.setPatientAge(patient.getPatientAge());
                dto.setPatientGender(patient.getGender());
            }
            
            enhancedAppointments.add(dto);
        }
        
        return enhancedAppointments;
    }

    public List<AppointmentStatus> listAppointmentsForDoctor(Integer doctorId) {
        return appointmentStatusRepository.findByDoctorIdOrderByStartTimeDesc(doctorId);
    }

    /**
     * Enhanced method that returns doctor's appointments with complete doctor and patient information
     */
    public List<AppointmentWithDoctorDto> listAppointmentsForDoctorWithPatientInfo(Integer doctorId) {
        List<AppointmentStatus> appointments = appointmentStatusRepository.findByDoctorIdOrderByStartTimeDesc(doctorId);
        List<AppointmentWithDoctorDto> enhancedAppointments = new ArrayList<>();
        
        for (AppointmentStatus appt : appointments) {
            // Fetch doctor information
            Doctor doctor = doctorRepository.findById(appt.getDoctorId()).orElse(null);
            Patient patient = patientRepository.findById(appt.getPatientId()).orElse(null);
            
            AppointmentWithDoctorDto dto = new AppointmentWithDoctorDto();
            
            // Set appointment fields
            dto.setScheduleId(appt.getScheduleId());
            dto.setId(appt.getScheduleId()); // Fix: Use scheduleId as appointment ID
            dto.setDoctorId(appt.getDoctorId());
            dto.setPatientId(appt.getPatientId());
            dto.setStartTime(appt.getStartTime());
            dto.setEndTime(appt.getEndTime());
            dto.setStatus(appt.getStatus());
            dto.setDoctorNotes(appt.getDoctorNotes());
            dto.setPrescription(appt.getPrescription());
            
            // Set doctor information
            if (doctor != null) {
                dto.setDoctorName(doctor.getName());
                dto.setDoctorSpecialty(doctor.getSpeaciality()); 
                dto.setDoctorEmail(doctor.getEmail());
                dto.setDoctorMobileNo(doctor.getMobileNo());
                dto.setDoctorBio(doctor.getBio());
                dto.setDoctorShift(doctor.getShift());
            }
            
            // Set patient information
            if (patient != null) {
                dto.setPatientName(patient.getPatientName());
                dto.setPatientEmail(patient.getEmail());
                dto.setPatientMobileNo(patient.getMobileNo());
                dto.setPatientAge(patient.getPatientAge());
                dto.setPatientGender(patient.getGender());
            }
            
            enhancedAppointments.add(dto);
        }
        
        return enhancedAppointments;
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
            List<Doctor> doctors = doctorRepository.findBySpecialty(specialty);
            
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

    // Get all doctors with their schedules and availability
    public List<Map<String, Object>> getDoctorsWithSchedules(String specialty, String date, int daysAhead) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try {
            List<Doctor> doctors;
            LocalDate startDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
            LocalDate endDate = startDate.plusDays(daysAhead);
            
            if (specialty != null && !specialty.trim().isEmpty()) {
                doctors = doctorRepository.findBySpecialty(specialty);
            } else {
                doctors = doctorRepository.findAll();
            }
            
            for (Doctor doctor : doctors) {
                Map<String, Object> doctorSchedule = new HashMap<>();
                doctorSchedule.put("doctorId", doctor.getDoctorId());
                doctorSchedule.put("doctorName", doctor.getName());
                doctorSchedule.put("specialty", doctor.getSpecialty());
                doctorSchedule.put("shift", doctor.getShift());
                
                // Calculate total appointments for this doctor in the date range
                List<AppointmentStatus> bookedAppointments = appointmentStatusRepository.findByDoctorIdAndDateRange(
                    doctor.getDoctorId(), startDate.atStartOfDay(), endDate.atStartOfDay());
                
                // Calculate available slots (simplified: assume 8 slots per day)
                int totalPossibleSlots = daysAhead * 8;
                int bookedSlots = bookedAppointments.size();
                int availableSlots = totalPossibleSlots - bookedSlots;
                
                doctorSchedule.put("totalSlots", totalPossibleSlots);
                doctorSchedule.put("bookedSlots", bookedSlots);
                doctorSchedule.put("availableSlots", Math.max(0, availableSlots));
                
                // Find next available slot
                LocalDateTime nextAvailable = findNextAvailableSlot(doctor.getDoctorId(), startDate);
                doctorSchedule.put("nextAvailableSlot", nextAvailable);
                
                // Generate daily schedule summary
                List<Map<String, Object>> dailySchedule = new ArrayList<>();
                LocalDate currentDate = startDate;
                while (!currentDate.isAfter(endDate)) {
                    Map<String, Object> daySchedule = new HashMap<>();
                    daySchedule.put("date", currentDate.toString());
                    
                    List<String> availableTimes = getAvailableTimesForDay(doctor.getDoctorId(), currentDate);
                    daySchedule.put("availableSlots", availableTimes);
                    
                    dailySchedule.add(daySchedule);
                    currentDate = currentDate.plusDays(1);
                }
                
                doctorSchedule.put("schedule", dailySchedule);
                result.add(doctorSchedule);
            }
            
        } catch (Exception e) {
            // Log error and return empty list
        }
        
        return result;
    }

    // Get specific doctor's detailed schedule
    public Map<String, Object> getDoctorDetailedSchedule(Integer doctorId, String startDate, String endDate, int daysAhead) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor == null) {
                result.put("error", "Doctor not found");
                return result;
            }
            
            LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now();
            LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : start.plusDays(daysAhead);
            
            result.put("doctorId", doctor.getDoctorId());
            result.put("doctorName", doctor.getName());
            result.put("specialty", doctor.getSpecialty());
            result.put("shift", doctor.getShift());
            
            // Get all appointments in the date range
            List<AppointmentStatus> appointments = appointmentStatusRepository.findByDoctorIdAndDateRange(
                doctorId, start.atStartOfDay(), end.atStartOfDay());
            
            result.put("totalAppointments", appointments.size());
            result.put("appointments", appointments);
            
            // Generate detailed daily schedule
            List<Map<String, Object>> detailedSchedule = new ArrayList<>();
            LocalDate currentDate = start;
            while (!currentDate.isAfter(end)) {
                final LocalDate finalCurrentDate = currentDate; // Make variable final for lambda
                Map<String, Object> dayDetail = new HashMap<>();
                dayDetail.put("date", finalCurrentDate.toString());
                
                // Get appointments for this day
                List<AppointmentStatus> dayAppointments = appointments.stream()
                    .filter(apt -> apt.getStartTime().toLocalDate().equals(finalCurrentDate))
                    .collect(Collectors.toList());
                
                dayDetail.put("appointmentsCount", dayAppointments.size());
                dayDetail.put("appointments", dayAppointments);
                
                List<String> availableTimes = getAvailableTimesForDay(doctorId, currentDate);
                dayDetail.put("availableSlots", availableTimes);
                dayDetail.put("availableSlotsCount", availableTimes.size());
                
                detailedSchedule.add(dayDetail);
                currentDate = currentDate.plusDays(1);
            }
            
            result.put("detailedSchedule", detailedSchedule);
            
        } catch (Exception e) {
            result.put("error", "Error retrieving doctor schedule: " + e.getMessage());
        }
        
        return result;
    }

    // Get doctors sorted by availability
    public List<Map<String, Object>> getDoctorsSortedByAvailability(String specialty, String date) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try {
            List<Doctor> doctors;
            LocalDate targetDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
            
            if (specialty != null && !specialty.trim().isEmpty()) {
                doctors = doctorRepository.findBySpecialty(specialty);
            } else {
                doctors = doctorRepository.findAll();
            }
            
            for (Doctor doctor : doctors) {
                Map<String, Object> doctorInfo = new HashMap<>();
                doctorInfo.put("doctorId", doctor.getDoctorId());
                doctorInfo.put("doctorName", doctor.getName());
                doctorInfo.put("specialty", doctor.getSpecialty());
                doctorInfo.put("shift", doctor.getShift());
                
                // Count appointments for the target date
                List<AppointmentStatus> dayAppointments = appointmentStatusRepository.findByDoctorIdAndDate(
                    doctor.getDoctorId(), targetDate.atStartOfDay(), targetDate.plusDays(1).atStartOfDay());
                
                int bookedSlots = dayAppointments.size();
                int availableSlots = 8 - bookedSlots; // Assume 8 slots per day
                
                doctorInfo.put("bookedSlots", bookedSlots);
                doctorInfo.put("availableSlots", Math.max(0, availableSlots));
                doctorInfo.put("availabilityPercentage", 
                    availableSlots > 0 ? (availableSlots * 100.0 / 8) : 0);
                
                // Get next available slot for this doctor
                LocalDateTime nextAvailable = findNextAvailableSlot(doctor.getDoctorId(), targetDate);
                doctorInfo.put("nextAvailableSlot", nextAvailable);
                
                result.add(doctorInfo);
            }
            
            // Sort by availability (most available first)
            result.sort((a, b) -> Integer.compare(
                (Integer) b.get("availableSlots"), 
                (Integer) a.get("availableSlots")));
                
        } catch (Exception e) {
            // Log error and return empty list
        }
        
        return result;
    }

    // Helper method to find next available slot for a doctor
    private LocalDateTime findNextAvailableSlot(Integer doctorId, LocalDate fromDate) {
        LocalDate currentDate = fromDate;
        LocalDate maxDate = fromDate.plusDays(30); // Search up to 30 days ahead
        
        while (!currentDate.isAfter(maxDate)) {
            List<String> availableTimes = getAvailableTimesForDay(doctorId, currentDate);
            if (!availableTimes.isEmpty()) {
                // Return first available time of the day
                return currentDate.atTime(LocalTime.parse(availableTimes.get(0)));
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return null; // No available slot found in next 30 days
    }

    // Helper method to get available times for a specific day
    private List<String> getAvailableTimesForDay(Integer doctorId, LocalDate date) {
        List<String> availableTimes = new ArrayList<>();
        
        // Define working hours (9 AM to 5 PM with 1-hour slots)
        LocalTime[] timeSlots = {
            LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0), LocalTime.of(12, 0),
            LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(16, 0), LocalTime.of(17, 0)
        };
        
        for (LocalTime timeSlot : timeSlots) {
            LocalDateTime startTime = date.atTime(timeSlot);
            LocalDateTime endTime = startTime.plusHours(1);
            
            // Check if this time slot is available
            List<AppointmentStatus> conflicts = appointmentStatusRepository.findConflictingAppointments(
                doctorId, startTime, endTime);
            
            if (conflicts.isEmpty()) {
                availableTimes.add(timeSlot.toString());
            }
        }
        
        return availableTimes;
    }
}
