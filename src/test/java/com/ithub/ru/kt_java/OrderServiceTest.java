package com.ithub.ru.kt_java;

import com.ithub.ru.kt_java.exception.ResourceNotFoundException;
import com.ithub.ru.kt_java.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void prepareTestData() {
        orderService.initializeTestOrders();
    }

    @Test
    @DisplayName("Получение всех заказов: проверка количества")
    void testFetchAllOrders() {
        assertEquals(3, orderService.getAll().size(), "Количество заказов не совпадает");
    }

    @ParameterizedTest
    @DisplayName("Получение заказа по ID: проверка существующих ID")
    @ValueSource(longs = {1, 2, 3})
    void testFetchOrderById(long id) {
        assertNotNull(orderService.getById(id), "Заказ не найден с ID: " + id);
    }

    @ParameterizedTest
    @DisplayName("Удаление заказа по ID: проверка исключения")
    @ValueSource(longs = {1, 2, 3})
    void testDeleteOrderById(long id) {
        assertDoesNotThrow(() -> orderService.getById(id), "Исключение при получении заказа до удаления");
        orderService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> orderService.getById(id),
                "Ожидалось исключение ResourceNotFoundException после удаления заказа");
    }
}
