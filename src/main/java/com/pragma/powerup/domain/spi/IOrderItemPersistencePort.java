package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderItemModel;

public interface IOrderItemPersistencePort {
    OrderItemModel saveOrderItem(OrderItemModel orderDishModel);
}
