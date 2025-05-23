package com.rajharit.rajharitsprings;

import com.rajharit.rajharitsprings.config.DataBaseSource;
import com.rajharit.rajharitsprings.dao.StockMovementImpl;
import com.rajharit.rajharitsprings.dao.IngredientDAO;
import com.rajharit.rajharitsprings.dao.IngredientDAOImpl;
import com.rajharit.rajharitsprings.config.DataBaseCleaner;
import com.rajharit.rajharitsprings.entities.StockMovement;
import com.rajharit.rajharitsprings.entities.Ingredient;
import com.rajharit.rajharitsprings.entities.MovementType;
import com.rajharit.rajharitsprings.entities.Unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StockMovementTest {
    private StockMovementImpl stockMovementImpl;
    private IngredientDAO ingredientDAO;
    private DataBaseSource dataBaseSource;
    private DataBaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        dataBaseSource = new DataBaseSource();
        stockMovementImpl = new StockMovementImpl(dataBaseSource);
        ingredientDAO = new IngredientDAOImpl(dataBaseSource);
        databaseCleaner = new DataBaseCleaner(dataBaseSource);

        databaseCleaner.cleanSpecificTables("Stock_Movement", "Ingredient");
    }

    @Test
    void testSaveStockMovement() {
        StockMovement stockMovement = new StockMovement(0, 1, MovementType.IN, 100, Unit.U, LocalDateTime.of(2025, 2, 1, 8, 0));
        stockMovementImpl.saveStockMovement(stockMovement);

        List<StockMovement> stockMovements = stockMovementImpl.getStockMovementsByIngredientId(1);
        assertFalse(stockMovements.isEmpty());
        assertEquals(1, stockMovements.size());
        assertEquals(100, stockMovements.getFirst().getQuantity());
    }

    @Test
    void testGetStockMovementsByIngredientId() {
        StockMovement stockMovement1 = new StockMovement(0, 1, MovementType.IN, 100, Unit.U, LocalDateTime.of(2025, 2, 1, 8, 0));
        StockMovement stockMovement2 = new StockMovement(0, 1, MovementType.OUT, 10, Unit.U, LocalDateTime.of(2025, 2, 2, 10, 0));
        stockMovementImpl.saveStockMovement(stockMovement1);
        stockMovementImpl.saveStockMovement(stockMovement2);

        List<StockMovement> stockMovements = stockMovementImpl.getStockMovementsByIngredientId(1);
        assertFalse(stockMovements.isEmpty());
        assertEquals(2, stockMovements.size());
        assertEquals(100, stockMovements.get(0).getQuantity());
        assertEquals(10, stockMovements.get(1).getQuantity());
    }

    @Test
    void testStockMovementsForSelAndRiz() {
        Ingredient sel = new Ingredient(0, "Sel", 2.5, Unit.G, LocalDateTime.now(), 0);
        Ingredient riz = new Ingredient(0, "Riz", 3.5, Unit.G, LocalDateTime.now(), 0);

        List<Ingredient> savedIngredients = ingredientDAO.saveAll(List.of(sel, riz));
        assertNotNull(savedIngredients);
        assertEquals(2, savedIngredients.size());

        int selId = savedIngredients.get(0).getId();
        int rizId = savedIngredients.get(1).getId();

        StockMovement selEntry1 = new StockMovement(0, selId, MovementType.IN, 500, Unit.G, LocalDateTime.of(2025, 2, 1, 8, 0));
        StockMovement selExit1 = new StockMovement(0, selId, MovementType.OUT, 100, Unit.G, LocalDateTime.of(2025, 2, 2, 10, 0));
        stockMovementImpl.saveStockMovement(selEntry1);
        stockMovementImpl.saveStockMovement(selExit1);

        StockMovement rizEntry1 = new StockMovement(0, rizId, MovementType.IN, 1000, Unit.G, LocalDateTime.of(2025, 2, 1, 8, 0));
        StockMovement rizExit1 = new StockMovement(0, rizId, MovementType.OUT, 200, Unit.G, LocalDateTime.of(2025, 2, 2, 10, 0));
        stockMovementImpl.saveStockMovement(rizEntry1);
        stockMovementImpl.saveStockMovement(rizExit1);

        List<StockMovement> selMovements = stockMovementImpl.getStockMovementsByIngredientId(selId);
        assertFalse(selMovements.isEmpty());
        assertEquals(2, selMovements.size());
        assertEquals(500, selMovements.get(0).getQuantity());
        assertEquals(100, selMovements.get(1).getQuantity());

        List<StockMovement> rizMovements = stockMovementImpl.getStockMovementsByIngredientId(rizId);
        assertFalse(rizMovements.isEmpty());
        assertEquals(2, rizMovements.size());
        assertEquals(1000, rizMovements.get(0).getQuantity());
        assertEquals(200, rizMovements.get(1).getQuantity());
    }
}