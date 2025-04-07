package com.rajharit.rajharitsprings.mapper;

import com.rajharit.rajharitsprings.dto.PriceDTO;
import com.rajharit.rajharitsprings.entities.PriceHistory;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {
    public PriceHistory toEntity(PriceDTO dto) {
        return new PriceHistory(dto.getPrice(), dto.getDate());
    }

    public PriceDTO toDto(PriceHistory entity) {
        PriceDTO dto = new PriceDTO();
        dto.setPrice(entity.getPrice());
        dto.setDate(entity.getDate());
        return dto;
    }
}