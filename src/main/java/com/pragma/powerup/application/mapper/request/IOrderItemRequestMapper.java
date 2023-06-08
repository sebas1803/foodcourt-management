package com.pragma.powerup.application.mapper.request;

import com.pragma.powerup.application.dto.request.OrderItemRequestDto;
import com.pragma.powerup.domain.model.OrderItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderItemRequestMapper {
    OrderItemModel toOrderItemModel(OrderItemRequestDto orderItemRequestDto);
}