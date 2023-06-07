package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderItemRequestDto;
import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.impl.OrderHandler;
import com.pragma.powerup.application.mapper.request.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.response.IOrderResponseMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderHandlerTest {

    @Spy
    private IOrderServicePort orderServicePort;
    @Spy
    private IOrderRequestMapper orderRequestMapper;
    @Spy
    private IOrderResponseMapper orderResponseMapper;
    @Spy
    private IRestaurantHandler restaurantHandler;
    @Spy
    private IOrderItemHandler orderItemHandler;

    @InjectMocks
    private OrderHandler orderHandler;

    @Test
    void testSaveOrder() {
        //Given
        SaveOrderRequestDto saveOrderRequestDto = new SaveOrderRequestDto();
        saveOrderRequestDto.setIdClient(6L);
        saveOrderRequestDto.setIdRestaurant(1L);

        List<OrderItemRequestDto> orderDishes = new ArrayList<>();
        OrderItemRequestDto orderItemRequestDto = new OrderItemRequestDto();
        orderItemRequestDto.setIdDish(1L);
        orderItemRequestDto.setQuantity(2);
        orderDishes.add(orderItemRequestDto);
        saveOrderRequestDto.setOrderDishes(orderDishes);

        OrderModel orderModel = new OrderModel();
        orderModel.setId(1L);
        orderModel.setIdClient(1L);
        orderModel.setIdEmployee(0L);
        orderModel.setStatus(OrderModel.PENDING);

        // Mock the necessary methods
        when(orderServicePort.findAllOrdersByClientId(saveOrderRequestDto.getIdClient()))
                .thenReturn(Collections.emptyList());
        when(orderRequestMapper.toOrderModel(saveOrderRequestDto))
                .thenReturn(orderModel);
        when(orderServicePort.saveOrder(orderModel))
                .thenReturn(orderModel);
        when(orderResponseMapper.toResponseOrder(orderModel))
                .thenReturn(new OrderResponseDto());

        // When
        OrderResponseDto response = orderHandler.saveOrder(saveOrderRequestDto);

        // Then
        assertNotNull(response);
        verify(orderServicePort).findAllOrdersByClientId(saveOrderRequestDto.getIdClient());
        verify(orderRequestMapper).toOrderModel(saveOrderRequestDto);
        verify(orderServicePort).saveOrder(orderModel);
        verify(orderResponseMapper).toResponseOrder(orderModel);
        verify(restaurantHandler).getRestaurantById(saveOrderRequestDto.getIdRestaurant());
        verify(orderItemHandler).saveOrderItem(Mockito.any(OrderItemRequestDto.class));
    }
}
