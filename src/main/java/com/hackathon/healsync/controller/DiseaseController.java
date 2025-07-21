package com.hackathon.healsync.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.dto.DiseaseDto;
import com.hackathon.healsync.service.DiseaseService;

@RestController
@RequestMapping("/v1/healsync/disease")
public class DiseaseController {
    @GetMapping("/all")
    public ResponseEntity<java.util.List<DiseaseDto>> getAllDiseases() {
        return ResponseEntity.ok(diseaseService.getAllDiseases());
    }
    private final DiseaseService diseaseService;

    public DiseaseController(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @PostMapping("/add")
    public ResponseEntity<DiseaseDto> addDisease(@RequestBody DiseaseDto diseaseDto) {
        DiseaseDto savedDisease = diseaseService.addDisease(diseaseDto);
        return ResponseEntity.ok(savedDisease);
    }
}
