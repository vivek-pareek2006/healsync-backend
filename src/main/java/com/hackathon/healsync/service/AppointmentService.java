    // ...existing code...
package com.hackathon.healsync.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
}
