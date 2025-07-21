package com.hackathon.healsync.service;

import java.time.LocalDateTime;
import java.util.List;

import com.hackathon.healsync.util.DoctorShift;
import org.springframework.stereotype.Service;

import com.hackathon.healsync.entity.AppointmentStatus;
import com.hackathon.healsync.entity.Doctor;
import com.hackathon.healsync.repository.AppointmentStatusRepository;
import com.hackathon.healsync.repository.DoctorRepository;

@Service
public class AppointmentService {
    private final DoctorRepository doctorRepository;
    private final AppointmentStatusRepository appointmentStatusRepository;

    public AppointmentService(DoctorRepository doctorRepository,
                            AppointmentStatusRepository appointmentStatusRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentStatusRepository = appointmentStatusRepository;
    }

    public Integer findAvailableDoctorId(String speciality, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DoctorShift requiredShift = DoctorShift.fromTimeRange(startDateTime, endDateTime);
        List<Doctor> doctors = doctorRepository.findBySpeacialityAndShift(speciality, requiredShift.name());
        for (var doctor : doctors) {
            var conflicts = appointmentStatusRepository.findConflictingAppointments(doctor.getDoctorId(), startDateTime, endDateTime);
            if (conflicts == null || conflicts.isEmpty()) {
                return doctor.getDoctorId();
            }
        }
        return null;
    }

    public String bookAppointment(Integer patientId, String speciality, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Integer doctorId = findAvailableDoctorId(speciality, startDateTime, endDateTime);
        if (doctorId == null) {
            return "No doctor available for the given speciality and time range.";
        }
        // Save appointment record in DB
        AppointmentStatus appointment = new AppointmentStatus();
        appointment.setDoctorId(doctorId);
        appointment.setPatientId(patientId);
        appointment.setStartTIme(startDateTime);
        appointment.setEndTIme(endDateTime);
        appointment.setStatus("booked");
        appointmentStatusRepository.save(appointment);
        return String.format("Appointment booked for patient %d with doctor %d from %s to %s", patientId, doctorId, startDateTime, endDateTime);
    }
}
