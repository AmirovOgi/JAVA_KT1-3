package com.ithub.ru.kt_java.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_orders")
@Schema(description = "Сущность заказа")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор заказа", example = "1")
    private long id;

    @NotNull(message = "The product shouldn't be null")
    @Schema(description = "Название продукта", example = "Laptop")
    private String product;

    @Min(value = 0, message = "The quantity must not be less than 0")
    @Schema(description = "Количество продукта", example = "10")
    private int quantity;

    @Positive(message = "The price must be greater than 0")
    @NotNull(message = "The price shouldn't be null")
    @Schema(description = "Цена продукта", example = "499.99")
    private BigDecimal price;

    @NotNull(message = "The status shouldn't be null")
    @Pattern(
            regexp = "CREATED|SHIPPED|DELIVERED",
            message = "Status must be one of: CREATED, SHIPPED, DELIVERED"
    )
    @Schema(description = "Статус заказа", allowableValues = {"CREATED", "SHIPPED", "DELIVERED"}, example = "CREATED")
    private String status;

    @NotNull(message = "The date shouldn't be null")
    @Schema(description = "Дата создания заказа", example = "2024-11-29")
    private LocalDate orderDate;

    public Order(String product, int quantity, BigDecimal price, String status, LocalDate orderDate) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.orderDate = orderDate;
    }
}
