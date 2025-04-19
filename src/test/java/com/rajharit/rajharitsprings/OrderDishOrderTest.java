package com.rajharit.rajharitsprings;

import com.rajharit.rajharitsprings.dao.*;
import com.rajharit.rajharitsprings.config.DataBaseSource;
import com.rajharit.rajharitsprings.config.DataBaseCleaner;
import com.rajharit.rajharitsprings.entities.*;
import com.rajharit.rajharitsprings.exceptions.InvalidStatusTransitionException;
import com.rajharit.rajharitsprings.exceptions.InsufficientStockException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDishOrderTest {
    private OrderDAO orderDAO;
    private OrderStatusDAO orderStatusDAO;
    private DishOrderDAO dishOrderDAO;
    private DataBaseSource dataBaseSource;
    private DataBaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        dataBaseSource = new DataBaseSource();
        orderDAO = new OrderDAOImpl(dataBaseSource);
        dishOrderDAO = new DishOrderDAOImpl(dataBaseSource);
        orderStatusDAO = new OrderStatusDAOImpl(dataBaseSource);
        databaseCleaner = new DataBaseCleaner(dataBaseSource);

        databaseCleaner.cleanSpecificTables("Dish_Order", "\"Order\"", "Order_Status");
    }

    @Test
    void testCreateOrder() {
        Order order = new Order();
        order.setReference("ORDER-001");
        order.setCreatedAt(LocalDateTime.now());
        order.setActualStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);
        assertNotNull(savedOrder.getOrderId());
        assertEquals("ORDER-001", savedOrder.getReference());
        assertEquals(StatusType.CREATED, savedOrder.getActualStatus());
    }

    @Test
    void testFindById() {
        Order order = new Order();
        order.setReference("ORDER-004");
        order.setCreatedAt(LocalDateTime.now());
        order.setActualStatus(StatusType.CREATED);

        Order existingOrder = orderDAO.findByReference(order.getReference());
        if (existingOrder != null) {
            throw new RuntimeException("La référence de commande existe déjà : " + order.getReference());
        }

        Order savedOrder = orderDAO.save(order);
        Order retrievedOrder = orderDAO.findById(savedOrder.getOrderId());

        assertNotNull(retrievedOrder);
        assertEquals("ORDER-004", retrievedOrder.getReference());
        assertEquals(StatusType.CREATED, retrievedOrder.getActualStatus());
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = new Order();
        order.setReference("ORDER-002");
        order.setCreatedAt(LocalDateTime.now());
        order.setActualStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);
        orderDAO.updateStatus(savedOrder.getOrderId(), StatusType.CONFIRMED);

        Order updatedOrder = orderDAO.findById(savedOrder.getOrderId());
        assertEquals(StatusType.CONFIRMED, updatedOrder.getActualStatus());
    }

    @Test
    void testAddDishToOrder() {
        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);

        Order order = new Order();
        order.setReference("ORDER-003");
        order.setCreatedAt(LocalDateTime.now());
        order.setActualStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);

        DishOrder dishOrder = new DishOrder();
        dishOrder.setDish(dish);
        dishOrder.setOrder(savedOrder);
        dishOrder.setQuantity(2);
        dishOrder.setStatus(StatusType.CREATED);

        DishOrder savedDishOrder = dishOrderDAO.save(dishOrder);
        assertNotNull(savedDishOrder.getDishOrderId());
        assertEquals(2, savedDishOrder.getQuantity());
        assertEquals(StatusType.CREATED, savedDishOrder.getStatus());
    }

    @Test
    void testSaveOrderStatus() {
        Order order = new Order();
        order.setReference("ORDER-001");
        order.setCreatedAt(LocalDateTime.now());
        order.setActualStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);
        assertNotNull(savedOrder.getOrderId());

        OrderStatus orderStatus = new OrderStatus(0, StatusType.CONFIRMED, LocalDateTime.now());

        OrderStatus savedOrderStatus = orderStatusDAO.save(orderStatus, savedOrder.getOrderId());
        assertNotNull(savedOrderStatus.getOrderStatusId());
        assertEquals(StatusType.CONFIRMED, savedOrderStatus.getStatus());
    }

    @Test
    void testFindOrderStatusByOrderId() {
        Order order = new Order();
        order.setReference("ORDER-002");
        order.setCreatedAt(LocalDateTime.now());
        order.setActualStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);
        assertNotNull(savedOrder.getOrderId());

        OrderStatus orderStatus1 = new OrderStatus(0, StatusType.CREATED, LocalDateTime.now());
        OrderStatus orderStatus2 = new OrderStatus(0, StatusType.CONFIRMED, LocalDateTime.now().plusMinutes(5));
        orderStatusDAO.save(orderStatus1, savedOrder.getOrderId());
        orderStatusDAO.save(orderStatus2, savedOrder.getOrderId());

        List<OrderStatus> orderStatuses = orderStatusDAO.findByOrderId(savedOrder.getOrderId());
        assertNotNull(orderStatuses);
        assertEquals(2, orderStatuses.size());
        assertEquals(StatusType.CREATED, orderStatuses.get(0).getStatus());
        assertEquals(StatusType.CONFIRMED, orderStatuses.get(1).getStatus());
    }

    @Test
    void testGetTotalAmount() {
        Dish dish1 = new Dish();
        dish1.setId(1);
        dish1.setName("Pizza");
        dish1.setUnitPrice(10000);

        Dish dish2 = new Dish();
        dish2.setId(2);
        dish2.setName("Burger");
        dish2.setUnitPrice(8000);

        Order order = new Order();
        order.setReference("ORDER-005");
        order.setCreatedAt(LocalDateTime.now());
        order.setActualStatus(StatusType.CREATED);

        DishOrder dishOrder1 = new DishOrder();
        dishOrder1.setDish(dish1);
        dishOrder1.setOrder(order);
        dishOrder1.setQuantity(2);

        DishOrder dishOrder2 = new DishOrder();
        dishOrder2.setDish(dish2);
        dishOrder2.setOrder(order);
        dishOrder2.setQuantity(3);

        order.addDishOrder(dishOrder1);
        order.addDishOrder(dishOrder2);

        double expectedTotalAmount = (dish1.getUnitPrice() * dishOrder1.getQuantity()) + (dish2.getUnitPrice() * dishOrder2.getQuantity());

        assertEquals(expectedTotalAmount, order.getTotalAmount(), 0.001);
    }

    @Test
    void testOrderConfirmationWithInsufficientStock() {
        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);

        dish.setIngredients(List.of(
                new Ingredient(1, "Saucisse", 20, Unit.G, LocalDateTime.now(), 100)
        ));

        Order order = new Order();
        order.setReference("ORDER-STOCK-TEST");
        order.setCreatedAt(LocalDateTime.now());

        DishOrder dishOrder = new DishOrder();
        dishOrder.setDish(dish);
        dishOrder.setQuantity(2);

        order.addDishOrder(dishOrder);

        assertThrows(InsufficientStockException.class, order::confirmOrder);
    }

    @Test
    void testConfirmOrder() {
        Order order = new Order();
        order.setReference("ORDER-CONFIRM-TEST");
        order.setCreatedAt(LocalDateTime.now());

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Ingrédient test");
        ingredient.setActualPrice(10);
        ingredient.setUnit(Unit.U);
        ingredient.setUpdateDateTime(LocalDateTime.now());
        ingredient.setAvailableQuantity(1);

        ingredient.getStockMovements().add(new StockMovement(
                0, 0, MovementType.IN, 10, Unit.U, LocalDateTime.now()
        ));

        Dish dish = new Dish();
        dish.setName("Plat test");
        dish.setUnitPrice(100);
        dish.setIngredients(List.of(ingredient));

        DishOrder dishOrder = new DishOrder();
        dishOrder.setDish(dish);
        dishOrder.setQuantity(1);
        order.addDishOrder(dishOrder);

        order.updateStatus(StatusType.CONFIRMED);

        assertEquals(StatusType.CONFIRMED, order.getActualStatus());
    }

    @Test
    void testInvalidStatusTransition() {
        Order order = new Order();
        order.setReference("ORDER-STATUS-TEST");
        order.setCreatedAt(LocalDateTime.now());
        order.setActualStatus(StatusType.CONFIRMED);

        assertThrows(InvalidStatusTransitionException.class,
                () -> order.updateStatus(StatusType.DELIVERED));
    }
}