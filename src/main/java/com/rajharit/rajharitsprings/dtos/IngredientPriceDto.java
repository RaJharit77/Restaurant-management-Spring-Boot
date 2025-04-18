package com.rajharit.rajharitsprings.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IngredientPriceDto {
    private String ingredientName;
    private double currentPrice;
    private String unit;
    private LocalDateTime updateDateTime;
}
