package com.rajharit.rajharitsprings.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IngredientDTO {
    private int id;
    private String name;
    private double unitPrice;
    private LocalDateTime updateDateTime;
}
