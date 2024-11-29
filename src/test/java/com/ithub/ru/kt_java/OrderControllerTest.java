package com.ithub.ru.kt_java;

import com.ithub.ru.kt_java.controller.OrderController;
import com.ithub.ru.kt_java.model.Order;
import com.ithub.ru.kt_java.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService.initializeTestOrders();
    }

    @Test
    public void TestReturnAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void TestReturnOrderById() throws Exception {
        Order order = new Order();
        order.setId(1);
        order.setProduct("Test Product");

        when(orderService.getById(1)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product").value("Test Product"));
    }

    @Test
    public void TestDeleteOrderById() throws Exception {
        doNothing().when(orderService).deleteById(2);

        mockMvc.perform(delete("/api/orders/2"))
                .andExpect(status().isNoContent());
    }
}