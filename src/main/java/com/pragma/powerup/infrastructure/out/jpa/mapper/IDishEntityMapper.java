package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishEntityMapper {
    DishEntity toEntityDish(DishModel dishModel);

    DishModel toDishModel(DishEntity dishEntity);

    default List<DishModel> toDishModelList(List<DishEntity> dishEntityList) {
        return dishEntityList.stream()
                .map(this::toDishModel)
                .collect(Collectors.toList());
    }

    default Map<String, List<DishModel>> toDishModelMap(Map<String, List<DishEntity>> dishEntityMap) {
        return dishEntityMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toDishModelList(entry.getValue())
                ));
    }
}
