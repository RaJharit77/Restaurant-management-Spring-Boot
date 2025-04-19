package com.rajharit.rajharitsprings.services;

import com.rajharit.rajharitsprings.dao.OrderDAO;
import com.rajharit.rajharitsprings.dtos.DishSoldDto;
import com.rajharit.rajharitsprings.entities.StatusType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {
    private final OrderDAO orderDAO;

    public SaleService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public List<DishSoldDto> findAllDeliveredDishes() {
        return orderDAO.getAll().stream()
                .filter(order -> order.getActualStatus() == StatusType.DELIVERED)
                .flatMap(order -> order.getDishOrders().stream()
                        .map(dishOrder -> {
                            DishSoldDto dto = new DishSoldDto();
                            dto.setDishIdentifier(dishOrder.getDish().getId());
                            dto.setDishName(dishOrder.getDish().getName());
                            dto.setQuantitySold(dishOrder.getQuantity());
                            return dto;
                        }))
                .collect(Collectors.toList());
    }
}
