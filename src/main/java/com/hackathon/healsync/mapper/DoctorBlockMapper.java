package com.hackathon.healsync.mapper;

import com.hackathon.healsync.dto.DoctorBlockDto;
import com.hackathon.healsync.entity.DoctorBlock;
import org.springframework.stereotype.Component;

@Component
public class DoctorBlockMapper {

    public DoctorBlockDto toDto(DoctorBlock block) {
        if (block == null) {
            return null;
        }
        
        DoctorBlockDto dto = new DoctorBlockDto();
        dto.setBlockId(block.getBlockId());
        dto.setDoctorId(block.getDoctor().getDoctorId());
        dto.setDoctorName(block.getDoctor().getName());
        dto.setStartDateTime(block.getStartDateTime());
        dto.setEndDateTime(block.getEndDateTime());
        dto.setReason(block.getReason());
        dto.setDescription(block.getDescription());
        dto.setIsActive(block.getIsActive());
        
        return dto;
    }

    public DoctorBlock toEntity(DoctorBlockDto dto) {
        if (dto == null) {
            return null;
        }
        
        DoctorBlock block = new DoctorBlock();
        block.setBlockId(dto.getBlockId());
        block.setStartDateTime(dto.getStartDateTime());
        block.setEndDateTime(dto.getEndDateTime());
        block.setReason(dto.getReason());
        block.setDescription(dto.getDescription());
        block.setIsActive(dto.getIsActive());
        
        return block;
    }
}
