package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.SaveRestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantListResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;

import java.util.List;

public interface IRestaurantHandler {
    void saveRestaurant(SaveRestaurantRequestDto saveRestaurantRequestDto);

    List<RestaurantListResponseDto> getAllRestaurants(int page, int size);

    RestaurantResponseDto getRestaurantById(Long restaurantId);
}
