package com.rajharit.rajharitsprings.services;

import com.rajharit.rajharitsprings.dao.IngredientDAO;
import com.rajharit.rajharitsprings.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    @Autowired
    private IngredientDAO ingredientDAO;

    public List<Ingredient> getAllIngredients() {
        return ingredientDAO.getAll();
    }

    public void addPriceHistory(int ingredientId, List<PriceHistory> priceHistory) {
        Ingredient ingredient = ingredientDAO.findById(ingredientId);
        if (ingredient == null) {
            throw new RuntimeException("Ingredient not found");
        }

        for (PriceHistory ph : priceHistory) {
            ingredient.addPriceHistory(ph.getPrice(), ph.getDate());
        }

        ingredientDAO.saveAll(List.of(ingredient));
    }

    public void addStockMovements(int ingredientId, List<StockMovement> stockMovements) {
        Ingredient ingredient = ingredientDAO.findById(ingredientId);
        if (ingredient == null) {
            throw new RuntimeException("Ingredient not found");
        }

        for (StockMovement sm : stockMovements) {
            ingredientDAO.addStockMovement(sm);
        }
    }
}

