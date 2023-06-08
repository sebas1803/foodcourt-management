package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.OrderItemModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderItemEntityMapper {
    @Mapping(target = "order.id", source = "idOrder")
    OrderItemEntity toEntity(OrderItemModel orderItemModel);

    OrderItemModel toOrderItemModel(OrderItemEntity orderItemEntity);
}
