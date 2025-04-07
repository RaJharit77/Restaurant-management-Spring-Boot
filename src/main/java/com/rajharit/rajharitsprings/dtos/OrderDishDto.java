package com.rajharit.rajharitsprings.dtos;

import lombok.Data;

@Data
public class OrderDishDto {
    private int dishId;
    private String dishName;
    private double dishPrice;
    private int quantity;
    private String status;
}
