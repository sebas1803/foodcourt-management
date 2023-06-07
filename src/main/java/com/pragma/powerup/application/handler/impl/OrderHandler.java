package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderItemRequestDto;
import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.exception.ApplicationException;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.handler.IOrderItemHandler;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.application.mapper.request.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.response.IOrderResponseMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.OrderModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    private final IOrderItemHandler orderItemHandler;

    private final IRestaurantHandler restaurantHandler;

    @Override
    public OrderResponseDto saveOrder(SaveOrderRequestDto saveOrderRequestDto) {
        Long clientId = saveOrderRequestDto.getIdClient();

        List<String> activeStatuses = Arrays.asList(OrderModel.PENDING, OrderModel.IN_PREPARATION, OrderModel.READY);
        List<OrderModel> activeOrders = orderServicePort.findAllByStatusInAndClientId(activeStatuses, clientId);
        if (!activeOrders.isEmpty()) {
            throw new ApplicationException("You already have an active order");
        }

        restaurantHandler.getRestaurantById(saveOrderRequestDto.getIdRestaurant());

        OrderModel orderModel = orderRequestMapper.toOrderModel(saveOrderRequestDto);
        orderModel.setIdEmployee(0L);
        orderModel.setStatus(OrderModel.PENDING);
        orderModel.setDate(Date.from(Instant.now()));
        orderModel = orderServicePort.saveOrder(orderModel);
        Long orderId = orderModel.getId();

        List<OrderItemRequestDto> orderItemList = saveOrderRequestDto.getOrderDishes();
        for (OrderItemRequestDto orderItem : orderItemList) {
            orderItem.setIdOrder(orderId);
            orderItemHandler.saveOrderItem(orderItem);
        }

        return orderResponseMapper.toResponseOrder(orderModel);
    }

    @Override
    public List<OrderResponseDto> findAllOrdersByStatus(String status, Long restaurantId, int page, int size) {
        return null;
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, String status) {
        return null;
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId, Long clientId) {
        return null;
    }

    @Override
    public void assignEmployeeToOrder(List<Long> orderId, Long employeeId) {

    }

    @Override
    public void markOrderAsReady(Long orderId) {

    }

    @Override
    public void markOrderAsReadyWithMessage(Long orderId) {

    }

    @Override
    public void markOrderAsDelivered(Long orderId, String securityCode) {

    }
}
