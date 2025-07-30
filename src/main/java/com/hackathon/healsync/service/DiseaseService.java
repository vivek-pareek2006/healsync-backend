package com.hackathon.healsync.service;

import org.springframework.stereotype.Service;

import com.hackathon.healsync.dto.DiseaseDto;
import com.hackathon.healsync.entity.Disease;
import com.hackathon.healsync.mapper.DiseaseMapper;
import com.hackathon.healsync.repository.DiseaseRepository;

@Service
public class DiseaseService {
    public DiseaseDto getDiseaseById(Integer id) {
        return diseaseRepository.findById(id)
            .map(DiseaseMapper::toDto)
            .orElse(null);
    }

    public DiseaseDto getDiseaseByName(String name) {
        Disease disease = diseaseRepository.findByName(name);
        return disease != null ? DiseaseMapper.toDto(disease) : null;
    }
    public java.util.List<DiseaseDto> getAllDiseases() {
        return diseaseRepository.findAll().stream()
            .map(DiseaseMapper::toDto)
            .collect(java.util.stream.Collectors.toList());
    }
    private final DiseaseRepository diseaseRepository;

    public DiseaseService(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    public DiseaseDto addDisease(DiseaseDto diseaseDto) {
        Disease disease = DiseaseMapper.toEntity(diseaseDto);
        Disease savedDisease = diseaseRepository.save(disease);
        return DiseaseMapper.toDto(savedDisease);
    }

    public DiseaseDto updateDisease(Integer diseaseId, DiseaseDto diseaseDto) {
        return diseaseRepository.findById(diseaseId)
            .map(existing -> {
                existing.setName(diseaseDto.getName());
                existing.setDescription(diseaseDto.getDescription());
                existing.setPrecaution(diseaseDto.getPrecaution());
                Disease saved = diseaseRepository.save(existing);
                return DiseaseMapper.toDto(saved);
            })
            .orElse(null);
    }

    public boolean deleteDisease(Integer diseaseId) {
        if (!diseaseRepository.existsById(diseaseId)) {
            return false;
        }
        diseaseRepository.deleteById(diseaseId);
        return true;
    }
}
