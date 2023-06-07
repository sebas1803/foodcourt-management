package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderItemServicePort;
import com.pragma.powerup.domain.model.OrderItemModel;
import com.pragma.powerup.domain.spi.IOrderItemPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderItemUseCase implements IOrderItemServicePort {
    private final IOrderItemPersistencePort orderItemPersistencePort;

    @Override
    public void saveOrderItem(OrderItemModel orderDishModel) {
        orderItemPersistencePort.saveOrderItem(orderDishModel);
    }
}
