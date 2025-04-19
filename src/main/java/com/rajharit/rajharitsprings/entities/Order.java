package com.rajharit.rajharitsprings.entities;

import com.rajharit.rajharitsprings.exceptions.InsufficientStockException;
import com.rajharit.rajharitsprings.exceptions.InvalidStatusTransitionException;
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
    private StatusType actualStatus;
    @Getter
    private List<DishOrder> dishOrders = new ArrayList<>();
    private List<OrderStatus> statusHistory;

    public Order() {
        this.actualStatus = StatusType.CREATED;
    }

    public Order(int orderId, String reference, LocalDateTime createdAt, StatusType actualStatus, List<DishOrder> dishOrders) {
        this.orderId = orderId;
        this.reference = reference;
        this.createdAt = createdAt;
        this.actualStatus = actualStatus;
        this.dishOrders = dishOrders;
    }

    public StatusType getActualStatus() {
        if (statusHistory == null || statusHistory.isEmpty()) {
            return StatusType.CREATED;
        }
        return statusHistory.getLast().getStatus();
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

    public void confirmOrder() {
        if (this.actualStatus != StatusType.CREATED) {
            throw new InvalidStatusTransitionException(
                    "Cannot confirm order from current status: " + this.actualStatus);
        }

        checkStockAvailability();

        this.actualStatus = StatusType.CONFIRMED;
        this.statusHistory.add(new OrderStatus(0, StatusType.CONFIRMED, LocalDateTime.now()));
    }

    private void checkStockAvailability() {
        List<String> missingIngredients = new ArrayList<>();

        for (DishOrder dishOrder : dishOrders) {
            Dish dish = dishOrder.getDish();
            double availableQuantity = dish.getAvailableQuantity(LocalDateTime.now());

            if (availableQuantity < dishOrder.getQuantity()) {
                missingIngredients.add(String.format(
                        "%s (besoin: %d, disponible: %.2f)",
                        dish.getName(), dishOrder.getQuantity(), availableQuantity));
            }
        }

        if (!missingIngredients.isEmpty()) {
            throw new InsufficientStockException(
                    "Stock insuffisant pour: " + String.join(", ", missingIngredients));
        }
    }

    private boolean isValidTransition(StatusType current, StatusType newStatus) {
        switch (current) {
            case CREATED:
                return newStatus == StatusType.CONFIRMED;
            case CONFIRMED:
                return newStatus == StatusType.IN_PROGRESS;
            case IN_PROGRESS:N:
                return newStatus == StatusType.FINISHED;
            case FINISHED:
                return newStatus == StatusType.DELIVERED;
            default:
                return false;
        }
    }

    public void updateStatus(StatusType newStatus) {
        if (!isValidTransition(this.actualStatus, newStatus)) {
            throw new InvalidStatusTransitionException(
                    "Transition invalide de " + this.actualStatus + " à " + newStatus);
        }

        if (newStatus == StatusType.CONFIRMED) {
            checkStockAvailability();
        }

        this.actualStatus = newStatus;
        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }
        statusHistory.add(new OrderStatus(0, newStatus, LocalDateTime.now()));
    }
}