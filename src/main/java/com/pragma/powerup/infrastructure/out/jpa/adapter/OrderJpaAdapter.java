package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderRepository.save(orderEntityMapper.toEntity(orderModel));
        return orderEntityMapper.toOrderModel(orderEntity);
    }

    @Override
    public OrderModel findOrderById(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId).orElseThrow(NoDataFoundException::new);
        return orderEntityMapper.toOrderModel(entity);
    }

    @Override
    public List<OrderModel> findAllOrdersByClientId(Long idClient) {
        List<OrderEntity> orderEntityList = orderRepository.findAllByIdClient(idClient);
        return orderEntityMapper.toOrderModelList(orderEntityList);
    }

    @Override
    public List<OrderModel> findAllByStatusInAndClientId(List<String> activeStatuses, Long clientId) {
        List<OrderEntity> orderEntityList = orderRepository.findAllByStatusInAndIdClient(activeStatuses, clientId);
        return orderEntityMapper.toOrderModelList(orderEntityList);
    }

    @Override
    public List<OrderModel> findAllByStatus(String status, Long restaurantId, int page, int size) {
        List<OrderEntity> entities = orderRepository.findAllByIdRestaurantAndStatus(PageRequest.of(page, size), restaurantId, status).toList();
        return orderEntityMapper.toOrderModelList(entities);
    }

    @Override
    public OrderModel updateStatus(String status, OrderModel orderModel) {
        OrderEntity entity = orderRepository.findById(orderModel.getId()).orElseThrow(NoDataFoundException::new);
        entity.setStatus(status);
        return orderEntityMapper.toOrderModel(entity);
    }

    @Override
    public OrderModel updateEmployee(Long employeeId, Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId).orElseThrow(NoDataFoundException::new);
        entity.setIdEmployee(employeeId);
        orderRepository.save(entity);
        return orderEntityMapper.toOrderModel(entity);
    }
}
