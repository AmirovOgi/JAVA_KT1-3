package com.ithub.ru.kt_java.controller;

import com.ithub.ru.kt_java.model.Order;
import com.ithub.ru.kt_java.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "Управление заказами")
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> fetchAllOrders() {
        log.debug("Получение всех заказов");
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> fetchOrderById(@PathVariable("id") long id) {
        log.debug("Получение заказа с ID: {}", id);
        Order order = orderService.getById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@Valid @RequestBody Order order) {
        log.debug("Добавление нового заказа: {}", order);
        Order savedOrder = orderService.save(order);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedOrder.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> modifyOrder(@PathVariable("id") long id, @Valid @RequestBody Order updatedOrder) {
        log.debug("Обновление заказа с ID: {}", id);
        Order result = orderService.update(id, updatedOrder);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeOrder(@PathVariable("id") long id) {
        log.debug("Удаление заказа с ID: {}", id);
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeAllOrders() {
        log.debug("Удаление всех заказов");
        orderService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
