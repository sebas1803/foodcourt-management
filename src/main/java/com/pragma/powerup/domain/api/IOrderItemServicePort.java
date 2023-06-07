package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderItemModel;

public interface IOrderItemServicePort {
    void saveOrderItem(OrderItemModel orderDishModel);
}
