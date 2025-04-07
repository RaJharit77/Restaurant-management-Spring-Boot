package com.rajharit.rajharitsprings.dao;

import com.rajharit.rajharitsprings.entities.Ingredient;
import com.rajharit.rajharitsprings.entities.StockMovement;
import com.rajharit.rajharitsprings.entities.Unit;

import java.time.LocalDateTime;
import java.util.List;

public interface IngredientDAO {
    List<Ingredient> getAll();

    Ingredient findById(int id);

    List<Ingredient> saveAll(List<Ingredient> ingredients);

    void deleteIngredient(int id);

    List<Ingredient> filterIngredients(String name, Unit unit, Double minPrice, Double maxPrice, int page, int pageSize);

    void addStockMovement(StockMovement movement);

    Ingredient findByIdAndPriceAndDateAndStockDate(int ingredientId, LocalDateTime priceDate, LocalDateTime stockDate);
}