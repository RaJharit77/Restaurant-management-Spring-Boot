package com.rajharit.rajharitsprings.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BestSalesDto {
    private String dishName;
    private int quantitySold;
    private double totalAmount;
}
