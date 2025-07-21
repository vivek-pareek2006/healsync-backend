package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.MedicineDto;
import com.hackathon.healsync.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/healsync/medicine")
public class MedicineController {
    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping("/add")
    public ResponseEntity<MedicineDto> addMedicine(@RequestBody MedicineDto medicineDto) {
        MedicineDto savedMedicine = medicineService.addMedicine(medicineDto);
        return ResponseEntity.ok(savedMedicine);
    }
}
