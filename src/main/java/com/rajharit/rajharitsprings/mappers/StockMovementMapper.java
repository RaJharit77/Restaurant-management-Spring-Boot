package com.rajharit.rajharitsprings.mapper;

import com.rajharit.rajharitsprings.dto.StockMovementDTO;
import com.rajharit.rajharitsprings.entities.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {
    public StockMovement toEntity(StockMovementDTO dto, int ingredientId) {
        return new StockMovement(
                0,
                ingredientId,
                dto.getMovementType(),
                dto.getQuantity(),
                dto.getUnit(),
                dto.getMovementDate()
        );
    }

    public StockMovementDTO toDto(StockMovement entity) {
        StockMovementDTO dto = new StockMovementDTO();
        dto.setMovementType(entity.getMovementType());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setMovementDate(entity.getMovementDate());
        return dto;
    }
}