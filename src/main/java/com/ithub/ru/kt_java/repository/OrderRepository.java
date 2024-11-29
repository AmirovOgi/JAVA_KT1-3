package com.ithub.ru.kt_java.repository;

import com.ithub.ru.kt_java.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Order, Long> {
    
}
