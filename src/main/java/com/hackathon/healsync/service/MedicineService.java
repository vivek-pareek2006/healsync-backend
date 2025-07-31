package com.hackathon.healsync.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<MedicineDto> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(MedicineMapper::toDto)
                .collect(Collectors.toList());
    }

    public MedicineDto updateMedicine(Integer medicineId, MedicineDto medicineDto) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(medicineId);
        if (optionalMedicine.isEmpty()) {
            return null;
        }
        Medicine medicine = optionalMedicine.get();
        medicine.setName(medicineDto.getName());
        medicine.setUsage(medicineDto.getUsage());
        medicine.setSideEffect(medicineDto.getSideEffect());
        Medicine updatedMedicine = medicineRepository.save(medicine);
        return MedicineMapper.toDto(updatedMedicine);
    }

    public boolean deleteMedicine(Integer medicineId) {
        if (!medicineRepository.existsById(medicineId)) {
            return false;
        }
        medicineRepository.deleteById(medicineId);
        return true;
    }

    public MedicineDto getMedicineById(Integer medicineId) {
        return medicineRepository.findById(medicineId)
                .map(MedicineMapper::toDto)
                .orElse(null);
    }
}
