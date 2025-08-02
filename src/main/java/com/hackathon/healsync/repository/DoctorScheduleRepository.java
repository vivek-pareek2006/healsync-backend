package com.hackathon.healsync.repository;

import com.hackathon.healsync.entity.DoctorSchedule;
import com.hackathon.healsync.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Integer> {
    List<DoctorSchedule> findByDoctorAndIsActiveTrue(Doctor doctor);
    List<DoctorSchedule> findByDoctorDoctorIdAndIsActiveTrue(Integer doctorId);
    void deleteByDoctorAndIsActiveTrue(Doctor doctor);
}
