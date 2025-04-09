package com.rajharit.rajharitsprings.services;

import com.rajharit.rajharitsprings.dtos.IngredientDto;
import com.rajharit.rajharitsprings.dtos.StockMovementDto;
import com.rajharit.rajharitsprings.dtos.PriceDto;
import com.rajharit.rajharitsprings.entities.*;
import com.rajharit.rajharitsprings.exceptions.ResourceNotFoundException;
import com.rajharit.rajharitsprings.mappers.IngredientMapper;
import com.rajharit.rajharitsprings.dao.IngredientDAOImpl;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    private final IngredientDAOImpl ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientDAOImpl ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public List<IngredientDto> getAllIngredients(Double maxPrice) {
        List<Ingredient> ingredients = ingredientRepository.getAll();

        if (maxPrice != null) {
            ingredients = ingredients.stream()
                    .filter(ingredient -> ingredient.getUnitPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        return ingredients.stream()
                .map(ingredientMapper::toDto)
                .collect(Collectors.toList());
    }

    public IngredientDto getIngredientById(int id) {
        Ingredient ingredient = ingredientRepository.findById(id);
        return ingredientMapper.toDto(ingredient);
    }

    public List<IngredientDto> saveIngredients(List<IngredientDto> ingredientDtos) {
        List<Ingredient> ingredients = ingredientDtos.stream()
                .map(ingredientMapper::toEntity)
                .collect(Collectors.toList());

        List<Ingredient> savedIngredients = ingredientRepository.saveAll(ingredients);

        return savedIngredients.stream()
                .map(ingredientMapper::toDto)
                .collect(Collectors.toList());
    }

    public void updatePrices(int ingredientId, List<PriceDto> priceDtos) {
        if (priceDtos == null || priceDtos.isEmpty()) {
            throw new IllegalArgumentException("Price list cannot be empty");
        }

        Ingredient ingredient = ingredientRepository.findById(ingredientId);
        if (ingredient == null) {
            throw new ResourceNotFoundException("Ingredient not found with id: " + ingredientId);
        }

        List<PriceHistory> prices = priceDtos.stream()
                .map(dto -> new PriceHistory(dto.getPrice(), dto.getDate()))
                .collect(Collectors.toList());

        ingredientRepository.savePrices(ingredientId, prices);

        PriceHistory latestPrice = prices.stream()
                .max(Comparator.comparing(PriceHistory::getDate))
                .orElseThrow();

        ingredient.setUnitPrice(latestPrice.getPrice());
        ingredient.setUpdateDateTime(latestPrice.getDate());
        ingredientRepository.updateCurrentPrice(ingredientId, latestPrice.getPrice(), latestPrice.getDate());
    }

    public void updateStockMovements(int ingredientId, List<StockMovementDto> stockMovementDtos) {
        if (stockMovementDtos == null || stockMovementDtos.isEmpty()) {
            throw new IllegalArgumentException("Stock movement list cannot be empty");
        }

        Ingredient ingredient = ingredientRepository.findById(ingredientId);
        if (ingredient == null) {
            throw new ResourceNotFoundException("Ingredient not found with id: " + ingredientId);
        }

        List<StockMovement> movements = stockMovementDtos.stream()
                .map(dto -> {
                    MovementType movementType = MovementType.valueOf(dto.getMovementType().toUpperCase());
                    Unit unit = Unit.valueOf(dto.getUnit().toUpperCase());
                    return new StockMovement(0, ingredientId, movementType,
                            dto.getQuantity(), unit, dto.getMovementDate());
                })
                .collect(Collectors.toList());

        ingredientRepository.saveStockMovements(ingredientId, movements);
    }
}
