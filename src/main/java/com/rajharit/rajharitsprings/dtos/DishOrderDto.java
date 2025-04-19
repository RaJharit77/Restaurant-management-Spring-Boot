package com.rajharit.rajharitsprings.dtos;

import com.rajharit.rajharitsprings.entities.StatusType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DishOrderDto {
    @NotNull(message = "L'ID du plat est requis")
    private Integer dishId;
    private String dishName;
    @NotNull(message = "La quantité est requise")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantity;
    private StatusType actualOrderStatus;
}