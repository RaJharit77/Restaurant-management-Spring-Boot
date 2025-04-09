package com.rajharit.rajharitsprings.mappers;

import com.rajharit.rajharitsprings.dtos.IngredientDto;
import com.rajharit.rajharitsprings.entities.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {
    public IngredientDto toDto(Ingredient ingredient) {
        IngredientDto dto = new IngredientDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setUnitPrice(ingredient.getUnitPrice());
        dto.setUpdateDateTime(ingredient.getUpdateDateTime());
        return dto;
    }

    public Ingredient toEntity(IngredientDto dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(dto.getId());
        ingredient.setName(dto.getName());
        ingredient.setUnitPrice(dto.getUnitPrice());
        ingredient.setUpdateDateTime(dto.getUpdateDateTime());
        return ingredient;
    }
}