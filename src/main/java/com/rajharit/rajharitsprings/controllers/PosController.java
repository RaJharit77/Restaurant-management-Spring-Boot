package com.rajharit.rajharitsprings.controllers;

import com.rajharit.rajharitsprings.dtos.*;
import com.rajharit.rajharitsprings.services.PosService;
import com.rajharit.rajharitsprings.security.ApiKeyManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PosController {
    private final PosService posService;
    private final ApiKeyManager apiKeyManager;

    public PosController(PosService posService, ApiKeyManager apiKeyManager) {
        this.posService = posService;
        this.apiKeyManager = apiKeyManager;
    }

    @GetMapping("/sales")
    public ResponseEntity<List<BestSalesDto>> getSalesData(
            @RequestHeader("X-API-KEY") String apiKey) {

        if (!apiKeyManager.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        List<BestSalesDto> sales = posService.getSalesData();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getOrderData(
            @RequestHeader("X-API-KEY") String apiKey) {

        if (!apiKeyManager.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        List<OrderDto> orders = posService.getOrderData();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/stock/movements")
    public ResponseEntity<List<StockMovementDto>> getStockMovements(int id,
            @RequestHeader("X-API-KEY") String apiKey) {

        if (!apiKeyManager.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        List<StockMovementDto> movements = posService.getStockMovements(id);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/ingredients/prices")
    public ResponseEntity<List<IngredientPriceDto>> getIngredientPrices(
            @RequestHeader("X-API-KEY") String apiKey) {

        if (!apiKeyManager.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        List<IngredientPriceDto> prices = posService.getIngredientPrices();
        return ResponseEntity.ok(prices);
    }
}