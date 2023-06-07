package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class OrderUseCase implements IOrderServicePort {
    private final IOrderPersistencePort orderPersistencePort;

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {
        return orderPersistencePort.saveOrder(orderModel);
    }

    @Override
    public OrderModel findOrderById(Long orderId) {
        return orderPersistencePort.findOrderById(orderId);
    }

    @Override
    public List<OrderModel> findAllOrdersByClientId(Long clientId) {
        return orderPersistencePort.findAllOrdersByClientId(clientId);
    }

    @Override
    public List<OrderModel> findAllByStatusInAndClientId(List<String> activeStatuses, Long clientId) {
        return orderPersistencePort.findAllByStatusInAndClientId(activeStatuses, clientId);
    }

    @Override
    public List<OrderModel> findAllByStatus(String status, Long restaurantId, int page, int size) {
        return orderPersistencePort.findAllByStatus(status, restaurantId, page, size);
    }

    @Override
    public OrderModel updateStatus(String status, OrderModel orderModel) {
        Map<String, List<String>> statusMap = permittedUpdate();
        if (statusMap.get(status) == null) {
            throw new DomainException("That status does not exist");
        }
        List<String> permittedStatuses = statusMap.get(orderModel.getStatus());
        if (permittedStatuses.contains(status)) {
            if (status.equals(OrderModel.CANCELLED)) {
                throw new DomainException("Sorry, your order is in preparation and cannot be cancelled");
            }
            orderModel.setStatus(status);
            return orderPersistencePort.saveOrder(orderModel);
        } else {
            throw new DomainException("The order is not in " + orderModel.getStatus() + " to be " + status);
        }
    }

    @Override
    public OrderModel updateEmployee(Long employeeId, Long orderId) {
        return orderPersistencePort.updateEmployee(employeeId, orderId);
    }

    private static Map<String, List<String>> permittedUpdate() {
        Map<String, List<String>> status = new HashMap<>();
        status.put(OrderModel.PENDING, Arrays.asList(OrderModel.IN_PREPARATION, OrderModel.CANCELLED));
        status.put(OrderModel.IN_PREPARATION, Collections.singletonList(OrderModel.READY));
        status.put(OrderModel.READY, Collections.singletonList(OrderModel.DELIVERED));
        status.put(OrderModel.DELIVERED, Collections.singletonList(OrderModel.READY));
        status.put(OrderModel.CANCELLED, Collections.emptyList());
        return status;
    }
}
