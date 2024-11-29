package com.ithub.ru.kt_java;

import com.ithub.ru.kt_java.model.Order;
import com.ithub.ru.kt_java.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE my_orders RESTART IDENTITY");
        orderRepository.deleteAll();
        orderRepository.saveAll(List.of(
                createOrder("Продукт 1", 1, "CREATED"),
                createOrder("Продукт 2", 2, "SHIPPED"),
                createOrder("Продукт 3", 3, "DELIVERED")
        ));
    }

    private Order createOrder(String product, int quantity, String status) {
        return new Order(product, quantity, BigDecimal.valueOf(100), status, LocalDate.now());
    }

    @Test
    public void TestReturnAllOrders() {
        List<Order> orders = orderRepository.findAll();
        Assertions.assertEquals(3, orders.size(), "The number of orders should be 3");
    }

    @Test
    public void TestFindOrderById() {
        Assertions.assertTrue(orderRepository.findById(1L).isPresent());
    }

    @Test
    public void TestDeleteOrderById() {
        long orderId = 2;
        orderRepository.deleteById(orderId);
        Assertions.assertFalse(orderRepository.findById(orderId).isPresent(), "Order with ID " + orderId + " should be deleted");
    }
}