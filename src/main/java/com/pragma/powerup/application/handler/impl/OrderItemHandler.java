package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderItemRequestDto;
import com.pragma.powerup.application.handler.IOrderItemHandler;
import com.pragma.powerup.application.mapper.request.IOrderItemRequestMapper;
import com.pragma.powerup.domain.api.IOrderItemServicePort;
import com.pragma.powerup.domain.model.OrderItemModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemHandler implements IOrderItemHandler {
    private final IOrderItemServicePort orderItemServicePort;
    private final IOrderItemRequestMapper orderItemRequestMapper;

    @Override
    public void saveOrderItem(OrderItemRequestDto orderItemRequestDto) {
        OrderItemModel orderItemModel = orderItemRequestMapper.toOrderItemModel(orderItemRequestDto);
        orderItemServicePort.saveOrderItem(orderItemModel);
    }
}
