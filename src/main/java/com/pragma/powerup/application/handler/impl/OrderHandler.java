package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderItemRequestDto;
import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.application.exception.ApplicationException;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.handler.IOrderItemHandler;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.application.mapper.request.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.response.IOrderResponseMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.infrastructure.out.api.TwilioApiClient;
import com.pragma.powerup.infrastructure.out.api.UsersApiClient;
import com.pragma.powerup.infrastructure.security.config.SecurityContext;
import com.pragma.powerup.infrastructure.security.impl.UserDetailsServiceImpl;
import com.pragma.powerup.infrastructure.security.jwt.UserManager;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    private final IOrderItemHandler orderItemHandler;

    private final IRestaurantHandler restaurantHandler;

    private final TwilioApiClient twilioApiClient;

    private final UsersApiClient usersApiClient;

    @Override
    public OrderResponseDto saveOrder(SaveOrderRequestDto saveOrderRequestDto) {
        Long clientId = saveOrderRequestDto.getIdClient();

        List<String> activeStatuses = Arrays.asList(OrderModel.PENDING, OrderModel.IN_PREPARATION, OrderModel.READY);
        List<OrderModel> activeOrders = orderServicePort.findAllByStatusInAndClientId(activeStatuses, clientId);
        if (!activeOrders.isEmpty()) {
            throw new ApplicationException("You already have an active order");
        }

        restaurantHandler.getRestaurantById(saveOrderRequestDto.getIdRestaurant());

        OrderModel orderModel = orderRequestMapper.toOrderModel(saveOrderRequestDto);
        orderModel.setIdEmployee(0L);
        orderModel.setStatus(OrderModel.PENDING);
        orderModel.setDate(Date.from(Instant.now()));
        orderModel = orderServicePort.saveOrder(orderModel);
        Long orderId = orderModel.getId();

        List<OrderItemRequestDto> orderItemList = saveOrderRequestDto.getOrderDishes();
        for (OrderItemRequestDto orderItem : orderItemList) {
            orderItem.setIdOrder(orderId);
            orderItemHandler.saveOrderItem(orderItem);
        }

        return orderResponseMapper.toResponseOrder(orderModel);
    }

    @Override
    public List<OrderResponseDto> findAllOrdersByStatus(String status, Long restaurantId, int page, int size) {
        List<OrderModel> orderModelList = orderServicePort.findAllByStatus(status, restaurantId, page, size);
        return orderResponseMapper.toResponseList(orderModelList);
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId, Long clientId) {
        OrderModel orderModel = orderServicePort.findOrderById(orderId);
        if (Objects.equals(orderModel.getIdClient(), clientId)) {
            orderModel = orderServicePort.updateStatus(OrderModel.CANCELLED, orderModel);
            return orderResponseMapper.toResponseOrder(orderModel);
        } else {
            throw new RuntimeException("You cannot cancel an order that is not yours");
        }
    }

    @Override
    public void assignEmployeeToOrder(List<Long> ordersId, Long idEmployee) {
        ordersId.forEach(
                orderId -> {
                    OrderModel orderModel = orderServicePort.findOrderById(orderId);
                    orderModel.setIdEmployee(idEmployee);
                    orderServicePort.updateStatus(OrderModel.IN_PREPARATION, orderModel);
                }
        );
    }

    @Override
    public void markOrderAsReadyWithMessage(Long orderId) {
        OrderModel orderModel = orderServicePort.findOrderById(orderId);
        String securityCode = getSecurityPin();
        orderModel.setSecurityCode(securityCode);
        orderServicePort.updateStatus(OrderModel.READY, orderModel);

        Long idClient = orderModel.getIdClient();
        String token = SecurityContext.getToken();
        UserResponseDto client = usersApiClient.getUserById(idClient, token);

        String toPhoneNumber = client.getPhone();
        String message = "Your order is ready for pickup. Please use the security PIN: " + securityCode;

        twilioApiClient.sendSMS(toPhoneNumber, message);
    }

    @Override
    public void markOrderAsDelivered(Long orderId, String securityCode) {
        OrderModel orderModel = orderServicePort.findOrderById(orderId);
        if (orderModel.getSecurityCode().equals(securityCode)) {
            orderServicePort.updateStatus(OrderModel.DELIVERED, orderModel);
        } else {
            throw new RuntimeException("Security code is not valid");
        }
    }

    public static String getSecurityPin() {
        Random random = new Random();
        int code = random.nextInt(9999 + 1);
        return String.format("%04d", code);
    }
}
