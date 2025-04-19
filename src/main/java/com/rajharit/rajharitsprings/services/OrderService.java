package com.rajharit.rajharitsprings.services;

import com.rajharit.rajharitsprings.dtos.*;
import com.rajharit.rajharitsprings.entities.*;
import com.rajharit.rajharitsprings.exceptions.BusinessException;
import com.rajharit.rajharitsprings.exceptions.ResourceNotFoundException;
import com.rajharit.rajharitsprings.mappers.OrderMapper;
import com.rajharit.rajharitsprings.dao.OrderDAO;
import com.rajharit.rajharitsprings.dao.DishDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderDAO orderDAO;
    private final DishDAO dishDAO;
    private final OrderMapper orderMapper;

    public OrderService(OrderDAO orderDAO, DishDAO dishDAO, OrderMapper orderMapper) {
        this.orderDAO = orderDAO;
        this.dishDAO = dishDAO;
        this.orderMapper = orderMapper;
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderByReference(String reference) {
        Order order = orderDAO.findByReference(reference);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with reference: " + reference);
        }

        order.getDishOrders().forEach(dishOrder -> {
            Dish dish = dishDAO.findById(dishOrder.getDish().getId());
            dishOrder.setDish(dish);
        });

        double totalAmount = order.getDishOrders().stream()
                .mapToDouble(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getQuantity())
                .sum();

        OrderDto dto = orderMapper.toDto(order);
        dto.setTotalAmount(totalAmount);
        return dto;
    }

    @Transactional
    public OrderDto updateOrderDishes(String reference, OrderUpdateDto orderUpdate) {
        if (orderUpdate == null || orderUpdate.getDishes() == null) {
            throw new BusinessException("Order update data cannot be null");
        }

        Order order = orderDAO.findByReference(reference);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with reference: " + reference);
        }

        if (orderUpdate.getStatus() != null) {
            validateStatusTransition(order.getActualStatus(), StatusType.valueOf(String.valueOf(orderUpdate.getStatus())));
            order.setActualStatus(StatusType.valueOf(String.valueOf(orderUpdate.getStatus())));
        }

        List<DishOrder> dishOrders = orderUpdate.getDishes().stream()
                .map(dto -> {
                    Dish dish = dishDAO.findById(dto.getDishId());
                    if (dish == null) {
                        throw new ResourceNotFoundException("Dish not found with id: " + dto.getDishId());
                    }

                    DishOrder dishOrder = new DishOrder();
                    dishOrder.setDish(dish);
                    dishOrder.setQuantity(dto.getQuantity());
                    dishOrder.setStatus(
                            order.getActualStatus() == StatusType.CONFIRMED ?
                                    StatusType.CONFIRMED :
                                    StatusType.CREATED
                    );
                    return dishOrder;
                })
                .collect(Collectors.toList());

        order.setDishOrders(dishOrders);
        Order updatedOrder = orderDAO.save(order);
        return orderMapper.toDto(updatedOrder);
    }

    @Transactional
    public void updateDishStatus(String reference, int dishId, StatusType status) {
        Order order = orderDAO.findByReference(reference);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with reference: " + reference);
        }

        DishOrder dishOrder = order.getDishOrders().stream()
                .filter(dishOrderItem -> dishOrderItem.getDish().getId() == dishId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Dish with id " + dishId + " not found in order " + reference));

        validateStatusTransition(dishOrder.getStatus(), status);

        dishOrder.setStatus(status);

        orderDAO.save(order);
    }

    @Transactional
    public OrderDto createOrder(String reference) {
        if (orderDAO.findByReference(reference) != null) {
            throw new BusinessException("Order reference already exists");
        }

        Order order = new Order();
        order.setReference(reference);
        order.setCreatedAt(LocalDateTime.now());
        order.setActualStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);
        return orderMapper.toDto(savedOrder);
    }

    private void validateStatusTransition(StatusType current, StatusType next) {
        if (current == next) {
            return;
        }

        switch (current) {
            case CREATED:
                if (next != StatusType.CONFIRMED) {
                    throw new BusinessException("Invalid status transition from CREATED to " + next);
                }
                break;
            case CONFIRMED:
                if (next != StatusType.IN_PROGRESS) {
                    throw new BusinessException("Invalid status transition from CONFIRMED to " + next);
                }
                break;
            case IN_PROGRESS:
                if (next != StatusType.FINISHED) {
                    throw new BusinessException("Invalid status transition from IN_PROGRESS to " + next);
                }
                break;
            case FINISHED:
                if (next != StatusType.DELIVERED) {
                    throw new BusinessException("Invalid status transition from FINISHED to " + next);
                }
                break;
            case DELIVERED:
                throw new BusinessException("Cannot change status from DELIVERED");
            default:
                throw new BusinessException("Unknown status: " + current);
        }
    }

    public void deleteOrderId(int id) {
        orderDAO.delete(id);
    }
}