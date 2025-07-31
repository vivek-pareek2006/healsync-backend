package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.MedicineDto;
import com.hackathon.healsync.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public ResponseEntity<List<MedicineDto>> getAllMedicines() {
        return ResponseEntity.ok(medicineService.getAllMedicines());
    }

    @GetMapping("/{medicineId}")
    public ResponseEntity<MedicineDto> getMedicineById(@PathVariable Integer medicineId) {
        MedicineDto medicine = medicineService.getMedicineById(medicineId);
        if (medicine == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medicine);
    }

    @PutMapping("/update/{medicineId}")
    public ResponseEntity<MedicineDto> updateMedicine(@PathVariable Integer medicineId, @RequestBody MedicineDto medicineDto) {
        MedicineDto updated = medicineService.updateMedicine(medicineId, medicineDto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{medicineId}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Integer medicineId) {
        boolean deleted = medicineService.deleteMedicine(medicineId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Medicine deleted successfully");
    }
}
