package com.rajharit.rajharitsprings.controllers;

import com.rajharit.rajharitsprings.dtos.*;
import com.rajharit.rajharitsprings.services.PosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pos")
public class PosController {
    private final PosService posService;
    private final String API_KEY = "SECRET_API_KEY"; // À remplacer par votre clé API

    public PosController(PosService posService) {
        this.posService = posService;
    }

    @GetMapping("/sales")
    public ResponseEntity<List<SaleDto>> getSalesData(
            @RequestHeader("X-API-KEY") String apiKey,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        List<SaleDto> sales = posService.getSalesData(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getOrderData(
            @RequestHeader("X-API-KEY") String apiKey,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        List<OrderDto> orders = posService.getOrderData(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/stock/movements")
    public ResponseEntity<List<StockMovementDto>> getStockMovements(
            @RequestHeader("X-API-KEY") String apiKey) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        List<StockMovementDto> movements = posService.getStockMovements();
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/ingredients/prices")
    public ResponseEntity<List<IngredientPriceDto>> getIngredientPrices(
            @RequestHeader("X-API-KEY") String apiKey) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        List<IngredientPriceDto> prices = posService.getIngredientPrices();
        return ResponseEntity.ok(prices);
    }
}