package com.hackathon.healsync.service;

import com.hackathon.healsync.dto.MedicineDto;
import com.hackathon.healsync.entity.Medicine;
import com.hackathon.healsync.mapper.MedicineMapper;
import com.hackathon.healsync.repository.MedicineRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;

    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public MedicineDto addMedicine(MedicineDto medicineDto) {
        Medicine medicine = MedicineMapper.toEntity(medicineDto);
        Medicine savedMedicine = medicineRepository.save(medicine);
        return MedicineMapper.toDto(savedMedicine);
    }
}
