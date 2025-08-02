package com.hackathon.healsync.repository;

import com.hackathon.healsync.entity.DoctorBlock;
import com.hackathon.healsync.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorBlockRepository extends JpaRepository<DoctorBlock, Integer> {
    List<DoctorBlock> findByDoctorAndIsActiveTrue(Doctor doctor);
    List<DoctorBlock> findByDoctorDoctorIdAndIsActiveTrue(Integer doctorId);
}
