package com.rajharit.rajharitsprings.controllers;

import com.rajharit.rajharitsprings.entities.Order;
import com.rajharit.rajharitsprings.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders/{reference}")
    public ResponseEntity<Order> getOrderByReference(@PathVariable String reference) {
        Order order = orderService.getOrderDetails(reference);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{reference}/dishes")
    public ResponseEntity<Void> updateOrderDishes(
            @PathVariable String reference,
            @RequestBody UpdateOrderDishesInput input) {
        try {
            orderService.updateOrderDishes(reference, input.getDishes(), input.getStatus());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/orders/{reference}/dishes/{dishId}")
    public ResponseEntity<Void> updateDishStatus(
            @PathVariable String reference,
            @PathVariable int dishId,
            @RequestBody UpdateDishStatusInput input) {
        try {
            orderService.updateDishStatus(reference, dishId, input.getStatus());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
