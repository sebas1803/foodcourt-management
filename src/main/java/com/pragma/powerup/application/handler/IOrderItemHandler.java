package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderItemRequestDto;

public interface IOrderItemHandler {
    void saveOrderItem(OrderItemRequestDto orderItemRequestDto);
}
