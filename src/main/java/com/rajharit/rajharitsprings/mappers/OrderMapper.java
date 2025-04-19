package com.rajharit.rajharitsprings.mappers;

import com.rajharit.rajharitsprings.dtos.DishOrderDto;
import com.rajharit.rajharitsprings.dtos.OrderDto;
import com.rajharit.rajharitsprings.entities.DishOrder;
import com.rajharit.rajharitsprings.entities.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setReference(order.getReference());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setActualStatus(order.getActualStatus());
        dto.setTotalAmount(order.getTotalAmount());

        if (order.getDishOrders() != null) {
            List<DishOrderDto> dishDtos = order.getDishOrders().stream()
                    .map(this::toDishOrderDto)
                    .collect(Collectors.toList());
            dto.setDishOrders(dishDtos);
        }

        double totalAmount = order.getDishOrders().stream()
                .mapToDouble(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getQuantity())
                .sum();
        dto.setTotalAmount(totalAmount);

        dto.setDishOrders(order.getDishOrders().stream()
                .map(this::toDishOrderDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private DishOrderDto toDishOrderDto(DishOrder dishOrder) {
        DishOrderDto dto = new DishOrderDto();
        dto.setDishId(dishOrder.getDish().getId());
        dto.setDishName(dishOrder.getDish().getName());
        dto.setQuantity(dishOrder.getQuantity());
        dto.setActualOrderStatus(dishOrder.getStatus());
        return dto;
    }

    /*private double calculateTotalAmount(Order order) {
        if (order.getDishOrders() == null) {
            return 0.0;
        }
        return order.getDishOrders().stream()
                .mapToDouble(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getQuantity())
                .sum();
    }*/
}