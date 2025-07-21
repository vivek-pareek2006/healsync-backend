package com.hackathon.healsync.service;

import org.springframework.stereotype.Service;

import com.hackathon.healsync.dto.DiseaseDto;
import com.hackathon.healsync.entity.Disease;
import com.hackathon.healsync.mapper.DiseaseMapper;
import com.hackathon.healsync.repository.DiseaseRepository;

@Service
public class DiseaseService {
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
}
