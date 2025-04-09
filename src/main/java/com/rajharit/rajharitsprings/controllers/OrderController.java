package com.rajharit.rajharitsprings.controllers;

import com.rajharit.rajharitsprings.dtos.OrderDto;
import com.rajharit.rajharitsprings.dtos.OrderUpdateDto;
import com.rajharit.rajharitsprings.entities.StatusType;
import com.rajharit.rajharitsprings.exceptions.BusinessException;
import com.rajharit.rajharitsprings.exceptions.ResourceNotFoundException;
import com.rajharit.rajharitsprings.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{reference}")
    public ResponseEntity<OrderDto> getOrderByReference(@PathVariable String reference) {
        try {
            OrderDto order = orderService.getOrderByReference(reference);
            return ResponseEntity.ok(order);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{reference}/dishes")
    public ResponseEntity<OrderDto> updateOrderDishes(
            @PathVariable String reference,
            @RequestBody OrderUpdateDto orderUpdate) {
        try {
            OrderDto updatedOrder = orderService.updateOrderDishes(reference, orderUpdate);
            return ResponseEntity.ok(updatedOrder);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{reference}/dishes/{dishId}")
    public ResponseEntity<Void> updateDishStatus(
            @PathVariable String reference,
            @PathVariable int dishId,
            @RequestParam StatusType status) {
        try {
            orderService.updateDishStatus(reference, dishId, status);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}