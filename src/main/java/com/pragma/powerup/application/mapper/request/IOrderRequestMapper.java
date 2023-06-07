package com.pragma.powerup.application.mapper.request;

import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper {
    @Mapping(target = "orderDishes", ignore = true)
    OrderModel toOrderModel(SaveOrderRequestDto saveOrderRequestDto);
}
