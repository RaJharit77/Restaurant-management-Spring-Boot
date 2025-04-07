package com.rajharit.rajharitsprings.dtos;

import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateDto {
    private String status;
    private List<OrderDishUpdateDto> dishes;
}
