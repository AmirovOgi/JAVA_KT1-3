package com.ithub.ru.kt_java.service;

import com.ithub.ru.kt_java.exception.ResourceNotFoundException;
import com.ithub.ru.kt_java.model.Order;
import com.ithub.ru.kt_java.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    private final JdbcTemplate jdbcTemplate;
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository, JdbcTemplate jdbcTemplate) {
        this.orderRepository = orderRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Order getById(long id) {
        log.debug("Попытка получить заказ с ID: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> handleResourceNotFound(id));
    }

    public List<Order> getAll() {
        log.debug("Получение всех заказов из базы данных");
        List<Order> orders = orderRepository.findAll();
        log.info("Всего заказов получено: {}", orders.size());
        return orders;
    }

    public Order save(Order order) {
        log.debug("Сохранение нового заказа: {}", order);
        order.setOrderDate(LocalDate.now());
        Order savedOrder = orderRepository.save(order);
        log.info("Заказ успешно сохранён: ID = {}", savedOrder.getId());
        return savedOrder;
    }

    public void deleteById(long id) {
        validateOrderExists(id);
        orderRepository.deleteById(id);
        log.info("Заказ успешно удалён: ID = {}", id);
    }

    public void deleteAll() {
        log.debug("Удаление всех заказов из базы данных");
        orderRepository.deleteAll();
        log.info("Все заказы успешно удалены");
    }

    public Order update(long id, Order order) {
        log.debug("Попытка обновить заказ с ID: {}", id);
        return orderRepository.findById(id).map(existingOrder -> {
            log.debug("Заказ найден, обновление данных");
            existingOrder.setProduct(order.getProduct());
            existingOrder.setQuantity(order.getQuantity());
            existingOrder.setPrice(order.getPrice());
            existingOrder.setStatus(order.getStatus());
            existingOrder.setOrderDate(LocalDate.now());
            Order updatedOrder = orderRepository.save(existingOrder);
            log.info("Заказ успешно обновлён: ID = {}", updatedOrder.getId());
            return updatedOrder;
        }).orElseThrow(() -> handleResourceNotFound(id));
    }

    public void initializeTestOrders() {
        log.debug("Запуск тестового наполнения базы данных заказами");
        resetDatabase();
        String[] products = {"Продукт 1", "Продукт 2", "Продукт 3"};
        String[] statuses = {"CREATED", "SHIPPED", "DELIVERED"};

        for (int i = 0; i < products.length; i++) {
            orderRepository.save(createOrder(products[i], i + 1, statuses[i]));
        }

        log.info("Тестовые данные успешно добавлены");
    }

    private Order createOrder(String product, int quantity, String status) {
        return new Order(product, quantity, BigDecimal.valueOf(100), status, LocalDate.now());
    }

    private void resetDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE my_orders RESTART IDENTITY");
        orderRepository.deleteAll();
        log.info("База данных успешно очищена");
    }

    private void validateOrderExists(long id) {
        if (!orderRepository.existsById(id)) {
            log.error("Заказ с ID = {} не найден при проверке", id);
            throw handleResourceNotFound(id);
        }
    }

    private ResourceNotFoundException handleResourceNotFound(long id) {
        String errorMessage = MessageFormat.format("Заказ с ID {0} не найден", id);
        log.error(errorMessage);
        return new ResourceNotFoundException(errorMessage);
    }
}


