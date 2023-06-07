package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.OrderItemModel;
import com.pragma.powerup.domain.spi.IOrderItemPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderItemEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderItemEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderItemRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderItemJpaAdapter implements IOrderItemPersistencePort {
    private final IOrderItemRepository orderItemRepository;
    private final IOrderItemEntityMapper orderItemEntityMapper;

    @Override
    public OrderItemModel saveOrderItem(OrderItemModel orderItemModel) {
        OrderItemEntity orderItemEntity = orderItemEntityMapper.toEntity(orderItemModel);
        return orderItemEntityMapper.toOrderItemModel(orderItemRepository.save(orderItemEntity));
    }
}
