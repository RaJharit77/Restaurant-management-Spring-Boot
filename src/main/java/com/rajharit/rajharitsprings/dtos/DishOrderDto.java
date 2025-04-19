package com.rajharit.rajharitsprings.dtos;

import com.rajharit.rajharitsprings.entities.StatusType;
import lombok.Data;

@Data
public class DishOrderDto {
    private int dishId;
    private String dishName;
    private int quantityOrdered;
    private StatusType actualOrderStatus;
}