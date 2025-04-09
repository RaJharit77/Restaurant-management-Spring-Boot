package com.rajharit.rajharitsprings.mappers;

import com.rajharit.rajharitsprings.dtos.StockMovementDto;
import com.rajharit.rajharitsprings.entities.StockMovement;
import com.rajharit.rajharitsprings.entities.MovementType;
import com.rajharit.rajharitsprings.entities.Unit;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {
    public StockMovement toEntity(StockMovementDto dto, int ingredientId) {
        return new StockMovement(
                0,
                ingredientId,
                MovementType.valueOf(dto.getMovementType()),
                dto.getQuantity(),
                Unit.valueOf(dto.getUnit()),
                dto.getMovementDate()
        );
    }

    public StockMovementDto toDto(StockMovement entity) {
        StockMovementDto dto = new StockMovementDto();
        dto.setMovementType(entity.getMovementType().name());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit().name());
        dto.setMovementDate(entity.getMovementDate());
        return dto;
    }
}