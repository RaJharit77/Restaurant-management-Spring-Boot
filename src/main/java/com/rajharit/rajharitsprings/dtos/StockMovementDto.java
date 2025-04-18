package com.rajharit.rajharitsprings.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockMovementDto {
    private int id;
    private String movementType;
    private double quantity;
    private String unit;
    private LocalDateTime movementDate;
}