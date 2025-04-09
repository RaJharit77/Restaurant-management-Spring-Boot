package com.rajharit.rajharitsprings.mappers;

import com.rajharit.rajharitsprings.entities.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {
    public StockMovement toEntity(StockMovementDto dto, int ingredientId) {
        return new StockMovement(
                0,
                ingredientId,
                dto.getMovementType(),
                dto.getQuantity(),
                dto.getUnit(),
                dto.getMovementDate()
        );
    }

    public StockMovementDto toDto(StockMovement entity) {
        StockMovementDto dto = new StockMovementDto();
        dto.setMovementType(entity.getMovementType());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setMovementDate(entity.getMovementDate());
        return dto;
    }
}