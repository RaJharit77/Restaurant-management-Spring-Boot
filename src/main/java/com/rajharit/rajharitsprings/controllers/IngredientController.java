package com.rajharit.rajharitsprings.controllers;

import com.rajharit.rajharitsprings.entities.Ingredient;
import com.rajharit.rajharitsprings.entities.PriceHistory;
import com.rajharit.rajharitsprings.entities.StockMovement;
import com.rajharit.rajharitsprings.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.getAllIngredientsWithDetails();
        return ResponseEntity.ok(ingredients);
    }

    @PutMapping("/ingredients/{id}/prices")
    public ResponseEntity<Void> addPriceHistory(
            @PathVariable int id,
            @RequestBody List<PriceHistory> priceHistory) {
        ingredientService.addPriceHistory(id, priceHistory);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ingredients/{id}/stockMovements")
    public ResponseEntity<Void> addStockMovements(
            @PathVariable int id,
            @RequestBody List<StockMovement> stockMovements) {
        ingredientService.addStockMovements(id, stockMovements);
        return ResponseEntity.ok().build();
    }
}

