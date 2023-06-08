package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.OrderItemModel;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {
    OrderEntity toEntity(OrderModel orderModel);

    OrderModel toOrderModel(OrderEntity orderEntity);

    default List<OrderModel> toOrderModelList(List<OrderEntity> orderEntityList) {
        return orderEntityList.stream()
                .map(this::toOrderModelFromEntity)
                .collect(Collectors.toList());
    }

    default OrderModel toOrderModelFromEntity(OrderEntity orderEntity) {
        OrderModel orderModel = new OrderModel();
        orderModel.setId(orderEntity.getId());
        orderModel.setDate(orderEntity.getDate());
        orderModel.setIdClient(orderEntity.getIdClient());
        orderModel.setIdRestaurant(orderEntity.getIdRestaurant());
        orderModel.setIdEmployee(orderEntity.getIdEmployee());
        orderModel.setStatus(orderEntity.getStatus());
        orderModel.setOrderDishes(toOrderItemModelList(orderEntity.getOrderItems()));
        return orderModel;
    }

    default List<OrderItemModel> toOrderItemModelList(List<OrderItemEntity> orderItemEntityList) {
        return orderItemEntityList.stream()
                .map(this::toOrderItemModel)
                .collect(Collectors.toList());
    }

    OrderItemModel toOrderItemModel(OrderItemEntity orderItemEntity);
}

