package com.rajharit.rajharitsprings.mappers;

import com.rajharit.rajharitsprings.dtos.DishOrderDto;
import com.rajharit.rajharitsprings.dtos.OrderDto;
import com.rajharit.rajharitsprings.entities.DishOrder;
import com.rajharit.rajharitsprings.entities.Order;
import com.rajharit.rajharitsprings.entities.StatusType;
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
        dto.setActualStatus(StatusType.valueOf(String.valueOf(order.getActualStatus())));
        dto.setTotalAmount(calculateTotalAmount(order));

        if (order.getDishOrders() != null) {
            List<DishOrderDto> dishDtos = order.getDishOrders().stream()
                    .map(this::toDishOrderDto)
                    .collect(Collectors.toList());
            dto.setDishOrders(dishDtos);
        }

        return dto;
    }

    private DishOrderDto toDishOrderDto(DishOrder dishOrder) {
        DishOrderDto dto = new DishOrderDto();
        dto.setDishId(dishOrder.getDish().getId());
        dto.setDishName(dishOrder.getDish().getName());
        dto.setQuantityOrdered(dishOrder.getQuantity());
        dto.setActualOrderStatus(dishOrder.getStatus());
        return dto;
    }

    private double calculateTotalAmount(Order order) {
        if (order.getDishOrders() == null) {
            return 0.0;
        }

        return order.getDishOrders().stream()
                .mapToDouble(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getQuantity())
                .sum();
    }
}