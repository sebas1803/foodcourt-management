package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderModel;

import java.util.List;

public interface IOrderServicePort {
    OrderModel saveOrder(OrderModel orderModel);

    OrderModel findOrderById(Long orderId);

    List<OrderModel> findAllOrdersByClientId(Long clientId);

    List<OrderModel> findAllByStatusInAndClientId(List<String> activeStatuses, Long clientId);

    List<OrderModel> findAllByStatus(String status, Long restaurantId, int page, int size);

    OrderModel updateStatus(String status, OrderModel orderModel);

    OrderModel updateEmployee(Long employeeId, Long orderId);
}
