package com.rajharit.rajharitsprings.mappers;

import com.rajharit.rajharitsprings.dtos.OrderDishDto;
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
        dto.setStatus(order.getStatus().name());

        if (order.getDishOrders() != null) {
            List<OrderDishDto> dishDtos = order.getDishOrders().stream()
                    .map(this::toOrderDishDto)
                    .collect(Collectors.toList());
            dto.setDishes(dishDtos);
        }

        return dto;
    }

    private OrderDishDto toOrderDishDto(DishOrder dishOrder) {
        OrderDishDto dto = new OrderDishDto();
        dto.setDishId(dishOrder.getDish().getId());
        dto.setDishName(dishOrder.getDish().getName());
        dto.setDishPrice(dishOrder.getDish().getUnitPrice());
        dto.setQuantity(dishOrder.getQuantity());
        dto.setStatus(dishOrder.getStatus().name());
        return dto;
    }
}