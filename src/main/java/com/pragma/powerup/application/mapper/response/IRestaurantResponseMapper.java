package com.pragma.powerup.application.mapper.response;

import com.pragma.powerup.application.dto.response.RestaurantListResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {
    RestaurantResponseDto toResponse(RestaurantModel restaurantModel);

    List<RestaurantResponseDto> toResponseList(Set<RestaurantModel> restaurantModelList);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "urlLogo", target = "urlLogo")
    RestaurantListResponseDto toRestaurantListResponseDto(RestaurantModel restaurantModel);

    List<RestaurantListResponseDto> toResponseAllList(List<RestaurantModel> restaurantModelList);
}
