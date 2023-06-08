package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;

import java.util.List;

public interface IOrderHandler {
    OrderResponseDto saveOrder(SaveOrderRequestDto saveOrderRequestDto);

    List<OrderResponseDto> findAllOrdersByStatus(String status, Long restaurantId, int page, int size);

    OrderResponseDto updateOrderStatus(Long orderId, String status);

    OrderResponseDto cancelOrder(Long orderId, Long clientId);

    void assignEmployeeToOrder(List<Long> ordersId, Long employeeId);

    void markOrderAsReady(Long orderId);

    void markOrderAsReadyWithMessage(Long orderId);

    void markOrderAsDelivered(Long orderId, String securityCode);
}
