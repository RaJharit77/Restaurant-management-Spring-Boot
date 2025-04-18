package com.rajharit.rajharitsprings.services;

import com.rajharit.rajharitsprings.dtos.*;
import com.rajharit.rajharitsprings.dao.*;
import com.rajharit.rajharitsprings.entities.*;
import com.rajharit.rajharitsprings.mappers.StockMovementMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PosService {
    private final OrderDAO orderDAO;
    private final DishOrderDAO dishOrderDAO;
    private final IngredientDAO ingredientDAO;
    private final StockMovementDAO stockMovementDAO;
    private final StockMovementMapper stockMovementMapper;

    public PosService(OrderDAO orderDAO,
                      DishOrderDAO dishOrderDAO,
                      IngredientDAO ingredientDAO,
                      StockMovementDAO stockMovementDAO,
                      StockMovementMapper stockMovementMapper) {
        this.orderDAO = orderDAO;
        this.dishOrderDAO = dishOrderDAO;
        this.ingredientDAO = ingredientDAO;
        this.stockMovementDAO = stockMovementDAO;
        this.stockMovementMapper = stockMovementMapper;
    }

    public List<BestSalesDto> getSalesData() {
        return orderDAO.getAll().stream()
                .filter(order -> order.getStatus() == StatusType.COMPLETED)
                .flatMap(order -> dishOrderDAO.findByOrderId(order.getOrderId()).stream())
                .map(dishOrder -> {
                    BestSalesDto dto = new BestSalesDto();
                    dto.setDishName(dishOrder.getDish().getName());
                    dto.setQuantitySold(dishOrder.getQuantity());
                    dto.setTotalAmount(dishOrder.getQuantity() * dishOrder.getDish().getUnitPrice());
                    return dto;
                })
                .collect(Collectors.toList()).reversed();
    }

    public List<OrderDto> getOrderData() {
        return orderDAO.getAll().stream()
                .map(order -> {
                    OrderDto dto = new OrderDto();
                    dto.setReference(order.getReference());
                    dto.setCreatedAt(order.getCreatedAt());
                    dto.setStatus(order.getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<StockMovementDto> getStockMovements(int id) {
        return stockMovementDAO.getStockMovementsByIngredientId(id).stream()
                .map(stockMovementMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<IngredientPriceDto> getIngredientPrices() {
        return ingredientDAO.getAll().stream()
                .map(ingredient -> {
                    IngredientPriceDto dto = new IngredientPriceDto();
                    dto.setIngredientName(ingredient.getName());
                    dto.setCurrentPrice(ingredient.getUnitPrice());
                    dto.setUnit(ingredient.getUnit().name());
                    dto.setUpdateDateTime(ingredient.getUpdateDateTime());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}