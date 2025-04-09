package com.rajharit.rajharitsprings.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IngredientDto {
    private int id;
    private String name;
    private double unitPrice;
    private LocalDateTime updateDateTime;
}
