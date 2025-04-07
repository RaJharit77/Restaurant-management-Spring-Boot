package com.rajharit.rajharitsprings.entities;

import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Order {
    private int orderId;
    private String reference;
    private LocalDateTime createdAt;
    private StatusType status;
    @Getter
    private List<DishOrder> dishOrders = new ArrayList<>();
    private List<OrderStatus> statusHistory;

    public Order() {
        this.status = StatusType.CREATED;
    }

    public Order(int orderId, String reference, LocalDateTime createdAt, StatusType status, List<DishOrder> dishOrders) {
        this.orderId = orderId;
        this.reference = reference;
        this.createdAt = createdAt;
        this.status = status;
        this.dishOrders = dishOrders;
    }

    public StatusType getActualStatus() {
        if (statusHistory == null || statusHistory.isEmpty()) {
            return StatusType.CREATED;
        }
        return statusHistory.get(statusHistory.size() - 1).getStatus();
    }

    public void addDishOrder(DishOrder dishOrder) {
        dishOrder.setOrder(this);
        this.dishOrders.add(dishOrder);
    }

    public double getTotalAmount() {
        return dishOrders.stream()
                .mapToDouble(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getQuantity())
                .sum();
    }
}