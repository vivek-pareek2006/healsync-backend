package com.hackathon.healsync.service;

import com.hackathon.healsync.dto.MedicineDto;
import com.hackathon.healsync.entity.Medicine;
import com.hackathon.healsync.mapper.MedicineMapper;
import com.hackathon.healsync.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return medicineRepository.findAll().stream()
                .map(MedicineMapper::toDto)
                .collect(Collectors.toList());
    }

    public MedicineDto getMedicineById(Integer medicineId) {
        Optional<Medicine> medicine = medicineRepository.findById(medicineId);
        return medicine.map(MedicineMapper::toDto).orElse(null);
    }

    public MedicineDto updateMedicine(Integer medicineId, MedicineDto medicineDto) {
        Optional<Medicine> medicineOpt = medicineRepository.findById(medicineId);
        if (medicineOpt.isEmpty()) return null;
        Medicine medicine = medicineOpt.get();
        medicine.setName(medicineDto.getName());
        medicine.setUsage(medicineDto.getUsage());
        medicine.setSideEffect(medicineDto.getSideEffect());
        Medicine saved = medicineRepository.save(medicine);
        return MedicineMapper.toDto(saved);
    }

    public boolean deleteMedicine(Integer medicineId) {
        if (!medicineRepository.existsById(medicineId)) return false;
        medicineRepository.deleteById(medicineId);
        return true;
    }
}
