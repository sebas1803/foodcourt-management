package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderItemModel;

public interface IOrderItemServicePort {
    OrderItemModel saveOrderItem(OrderItemModel orderDishModel);
}
