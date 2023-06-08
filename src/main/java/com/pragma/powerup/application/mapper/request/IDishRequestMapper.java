package com.pragma.powerup.application.mapper.request;

import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishRequestMapper {
    @Mapping(target = "restaurant", source = "idRestaurant", qualifiedByName = "mapRestaurant")
    DishModel toDishModel(SaveDishRequestDto saveDishRequestDto);

    @Named("mapRestaurant")
    default RestaurantModel mapRestaurantId(Long restaurantId) {
        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setId(restaurantId);
        return restaurantModel;
    }
}
