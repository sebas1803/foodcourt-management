package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderItemRequestDto;
import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.impl.OrderHandler;
import com.pragma.powerup.application.mapper.request.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.response.IOrderResponseMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.OrderItemModel;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Given
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

        List<OrderModel> activeOrders = new ArrayList<>();
        when(orderServicePort.findAllByStatusInAndClientId(anyList(), eq(saveOrderRequestDto.getIdClient())))
                .thenReturn(activeOrders);
        when(restaurantHandler.getRestaurantById(eq(saveOrderRequestDto.getIdRestaurant())))
                .thenReturn(new RestaurantResponseDto());
        when(orderRequestMapper.toOrderModel(eq(saveOrderRequestDto)))
                .thenReturn(orderModel);
        when(orderServicePort.saveOrder(eq(orderModel)))
                .thenReturn(orderModel);
        when(orderResponseMapper.toResponseOrder(eq(orderModel)))
                .thenReturn(new OrderResponseDto());

        // When
        OrderResponseDto response = orderHandler.saveOrder(saveOrderRequestDto);

        // Then
        assertNotNull(response);
        verify(orderServicePort).findAllByStatusInAndClientId(anyList(), eq(saveOrderRequestDto.getIdClient()));
        verify(restaurantHandler).getRestaurantById(eq(saveOrderRequestDto.getIdRestaurant()));
        verify(orderRequestMapper).toOrderModel(eq(saveOrderRequestDto));
        verify(orderServicePort).saveOrder(eq(orderModel));
        verify(orderResponseMapper).toResponseOrder(eq(orderModel));
        verify(orderItemHandler).saveOrderItem(any(OrderItemRequestDto.class));
    }

    @Test
    public void testFindAllByStatus() {
        // Given
        Long restaurantId = 1L;
        int page = 0;
        int size = 10;
        String status = OrderModel.PENDING;
        List<OrderItemModel> orderDishModels1 = new ArrayList<>();
        orderDishModels1.add(new OrderItemModel(1L, 1L, 1));
        List<OrderItemModel> orderDishModels2 = new ArrayList<>();
        orderDishModels2.add(new OrderItemModel(1L, 1L, 2));
        List<OrderModel> orders = new ArrayList<>();
        orders.add(new OrderModel(1L, Date.from(new Date().toInstant()), 1L, 1L, 1L, "PENDING", null, orderDishModels1));
        orders.add(new OrderModel(2L, Date.from(new Date().toInstant()), 1L, 1L, 1L, "PENDING", null, orderDishModels2));

        when(orderServicePort.findAllByStatus(eq(status), eq(restaurantId), eq(page),
                eq(size))).thenReturn(orders);

        List<OrderResponseDto> expectedResponse = orderResponseMapper.toResponseList(orders);

        // When
        List<OrderResponseDto> actualResponse = orderHandler.findAllOrdersByStatus(status, restaurantId, page, size);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(orderServicePort).findAllByStatus(eq(status), eq(restaurantId), eq(page), eq(size));
    }

    @Test
    public void testAssignEmployeeToOrder() {
        // Given
        Long id = 2L;
        List<Long> ordersId = Arrays.asList(1L, 2L, 3L);
        OrderModel orderModel1 = new OrderModel(1L, new Date(), 1L, null, 1L, "PENDING", null, new ArrayList<>());
        OrderModel orderModel2 = new OrderModel(2L, new Date(), 2L, null, 1L, "PENDING", null, new ArrayList<>());
        OrderModel orderModel3 = new OrderModel(3L, new Date(), 3L, null, 2L, "PENDING", null, new ArrayList<>());
        when(orderServicePort.findOrderById(1L)).thenReturn(orderModel1);
        when(orderServicePort.findOrderById(2L)).thenReturn(orderModel2);
        when(orderServicePort.findOrderById(3L)).thenReturn(orderModel3);

        // When
        orderHandler.assignEmployeeToOrder(ordersId, id);

        // Then
        verify(orderServicePort, times(1)).updateStatus(eq(OrderModel.IN_PREPARATION), eq(orderModel1));
        verify(orderServicePort, times(1)).updateStatus(eq(OrderModel.IN_PREPARATION), eq(orderModel2));
        verify(orderServicePort, times(1)).updateStatus(eq(OrderModel.IN_PREPARATION), eq(orderModel3));
        assertEquals(id, orderModel1.getIdEmployee());
        assertEquals(id, orderModel2.getIdEmployee());
        assertEquals(id, orderModel3.getIdEmployee());
    }

    @Test
    public void testMarkOrderAsDelivered() {
        // Given
        Long orderId = 1L;
        String securityCode = "123456";
        OrderModel orderModel = new OrderModel(1L, new Date(), 1L, 3L, 1L, "READY", securityCode, new ArrayList<>());
        when(orderServicePort.findOrderById(orderId)).thenReturn(orderModel);

        // When
        orderHandler.markOrderAsDelivered(orderId, securityCode);

        // Then
        verify(orderServicePort, times(1)).updateStatus(eq(OrderModel.DELIVERED), eq(orderModel));
    }
}
