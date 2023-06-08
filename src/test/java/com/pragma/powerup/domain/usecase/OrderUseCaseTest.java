package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderUseCaseTest {

    @Spy
    private IOrderPersistencePort orderPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    @BeforeEach
    public void setUp() {
        orderUseCase = new OrderUseCase(orderPersistencePort);
    }

    @Test
    public void testSaveOrder() {
        // Given
        OrderModel orderModel = new OrderModel(1L, new Date(), 1L, 1L, 1L, "PENDING", new ArrayList<>());
        when(orderPersistencePort.saveOrder(orderModel)).thenReturn(orderModel);

        // When
        OrderModel savedOrder = orderUseCase.saveOrder(orderModel);

        // Then
        assertNotNull(savedOrder);
        assertEquals(OrderModel.PENDING, savedOrder.getStatus());
        assertNotNull(savedOrder.getDate());
        verify(orderPersistencePort, times(1)).saveOrder(orderModel);
    }

    @Test
    public void testFindAllByStatus() {
        // Given
        String status = OrderModel.PENDING;
        Long restaurantId = 1L;
        int page = 0;
        int size = 10;
        List<OrderModel> orders = Arrays.asList(new OrderModel(), new OrderModel());
        when(orderPersistencePort.findAllByStatus(status, restaurantId, page, size)).thenReturn(orders);

        // When
        List<OrderModel> foundOrders = orderUseCase.findAllByStatus(status, restaurantId, page, size);

        // Then
        assertNotNull(foundOrders);
        assertEquals(orders.size(), foundOrders.size());
        assertEquals(orders.get(0), foundOrders.get(0));
        assertEquals(orders.get(1), foundOrders.get(1));
        verify(orderPersistencePort, times(1)).findAllByStatus(status, restaurantId, page, size);
    }
}
