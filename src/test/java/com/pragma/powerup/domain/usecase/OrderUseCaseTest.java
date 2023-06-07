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
import java.util.Date;

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
}
