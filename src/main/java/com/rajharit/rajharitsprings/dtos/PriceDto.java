package com.rajharit.rajharitsprings.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PriceDTO {
    private double price;
    private LocalDateTime date;
}