package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.OrderItemModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderItemEntityMapper {
    OrderItemEntity toEntity(OrderItemModel orderItemModel);

    OrderItemModel toOrderItemModel(OrderItemEntity orderItemEntity);
}
