package com.hackathon.healsync.controller;

import org.springframework.http.ResponseEntity;

import com.hackathon.healsync.dto.DiseaseDto;
import com.hackathon.healsync.service.DiseaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/v1/healsync/disease")
public class DiseaseController {
    @GetMapping("/details")
    public ResponseEntity<DiseaseDto> getDiseaseDetails(@RequestParam(required = false) Integer id,
                                                       @RequestParam(required = false) String name) {
        DiseaseDto result = null;
        if (id != null) {
            result = diseaseService.getDiseaseById(id);
        } else if (name != null) {
            result = diseaseService.getDiseaseByName(name);
        }
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
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

    @PutMapping("/update/{diseaseId}")
    public ResponseEntity<DiseaseDto> updateDisease(@PathVariable Integer diseaseId, @RequestBody DiseaseDto diseaseDto) {
        DiseaseDto updated = diseaseService.updateDisease(diseaseId, diseaseDto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{diseaseId}")
    public ResponseEntity<?> deleteDisease(@PathVariable Integer diseaseId) {
        boolean deleted = diseaseService.deleteDisease(diseaseId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Disease deleted successfully");
    }
}
