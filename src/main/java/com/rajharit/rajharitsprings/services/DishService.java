package com.rajharit.rajharitsprings.services;

import com.rajharit.rajharitsprings.dao.DishDAO;
import com.rajharit.rajharitsprings.entities.Dish;
import com.rajharit.rajharitsprings.entities.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    @Autowired
    private DishDAO dishDAO;

    public List<Dish> getAllDishes() {
        return dishDAO.getAll();
    }

    public void updateDishIngredients(int dishId, List<Ingredient> ingredients) {
        Dish dish = dishDAO.findById(dishId);
        if (dish == null) {
            throw new RuntimeException("Dish not found");
        }

        dish.setIngredients(ingredients);
        dishDAO.saveAll(List.of(dish));
    }
}
