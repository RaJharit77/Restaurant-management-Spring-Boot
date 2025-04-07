package com.rajharit.rajharitsprings.controllers;

import com.rajharit.rajharitsprings.entities.Dish;
import com.rajharit.rajharitsprings.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/dishes")
    public ResponseEntity<List<Dish>> getAllDishes() {
        List<Dish> dishes = dishService.getAllDishesWithDetails();
        return ResponseEntity.ok(dishes);
    }

    @PutMapping("/dishes/{id}/ingredients")
    public ResponseEntity<Void> updateDishIngredients(
            @PathVariable int id,
            @RequestBody List<DishIngredient> ingredients) {
        dishService.updateDishIngredients(id, ingredients);
        return ResponseEntity.ok().build();
    }
}
