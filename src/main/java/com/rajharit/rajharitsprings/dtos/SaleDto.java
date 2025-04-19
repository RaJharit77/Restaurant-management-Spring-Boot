package com.rajharit.rajharitsprings.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SaleDto {
    private int id;
    private String dishName;
    private int quantitySold;
    private LocalDateTime saleDate;
}

