package com.pragma.powerup.application.mapper.response;

import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {
    OrderResponseDto toResponseOrder(OrderModel orderModel);

    List<OrderResponseDto> toResponseList(List<OrderModel> ordersModel);
}
