package com.hackathon.healsync.service;

import org.springframework.stereotype.Service;

import com.hackathon.healsync.dto.MedicineDto;
import com.hackathon.healsync.entity.Medicine;
import com.hackathon.healsync.mapper.MedicineMapper;
import com.hackathon.healsync.repository.MedicineRepository;

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

    public MedicineDto getMedicineById(Integer medicineId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMedicineById'");
    }
}
