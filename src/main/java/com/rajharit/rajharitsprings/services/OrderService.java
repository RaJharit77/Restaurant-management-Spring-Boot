package com.rajharit.rajharitsprings.services;

import com.rajharit.rajharitsprings.dao.DishDAO;
import com.rajharit.rajharitsprings.dao.DishOrderDAO;
import com.rajharit.rajharitsprings.dao.OrderDAO;
import com.rajharit.rajharitsprings.entities.Dish;
import com.rajharit.rajharitsprings.entities.DishOrder;
import com.rajharit.rajharitsprings.entities.Order;
import com.rajharit.rajharitsprings.entities.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private DishOrderDAO dishOrderDAO;

    @Autowired
    private DishDAO dishDAO;

    public Order getOrderByReference(String reference) {
        return orderDAO.findByReference(reference);
    }

    public void updateOrderDishes(String reference, List<DishOrder> dishOrders, StatusType status) {
        Order order = orderDAO.findByReference(reference);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (status != StatusType.CREATED && status != StatusType.CONFIRMED) {
            throw new RuntimeException("Invalid status for order update");
        }

        List<DishOrder> existingDishOrders = dishOrderDAO.findByOrderId(order.getOrderId());
        for (DishOrder existing : existingDishOrders) {
            dishOrderDAO.updateStatus(existing.getDishOrderId(), StatusType.CREATED);
        }

        for (DishOrder dishOrder : dishOrders) {
            Dish dish = dishDAO.findById(dishOrder.getDish().getId());
            if (dish == null) {
                throw new RuntimeException("Dish not found: " + dishOrder.getDish().getId());
            }

            dishOrder.setDish(dish);
            dishOrder.setOrder(order);
            dishOrder.setStatus(status);
            dishOrderDAO.save(dishOrder);
        }

        order.setStatus(status);
        orderDAO.updateStatus(order.getOrderId(), status);
    }

    public void updateDishOrderStatus(String reference, int dishId, StatusType status) {
        Order order = orderDAO.findByReference(reference);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        List<DishOrder> dishOrders = dishOrderDAO.findByOrderId(order.getOrderId());
        DishOrder target = null;

        for (DishOrder dishOrder : dishOrders) {
            if (dishOrder.getDish().getId() == dishId) {
                target = dishOrder;
                break;
            }
        }

        if (target == null) {
            throw new RuntimeException("Dish not found in order");
        }

        dishOrderDAO.updateStatus(target.getDishOrderId(), status);
    }
}
