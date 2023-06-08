package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.SaveRestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantListResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.application.mapper.request.IRestaurantRequestMapper;
import com.pragma.powerup.application.mapper.response.IRestaurantResponseMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {
    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    @Override
    public void saveRestaurant(SaveRestaurantRequestDto saveRestaurantRequestDto) {
        restaurantServicePort.saveRestaurant(restaurantRequestMapper.toRestaurantModel(saveRestaurantRequestDto));
    }

    @Override
    public List<RestaurantListResponseDto> getAllRestaurants(int page, int size) {
        List<RestaurantModel> restaurantModels = restaurantServicePort.getAllRestaurants(page, size);
        return restaurantResponseMapper.toResponseAllList(restaurantModels);
    }

    @Override
    public RestaurantResponseDto getRestaurantById(Long restaurantId) {
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurantById(restaurantId);
        return restaurantResponseMapper.toResponse(restaurantModel);
    }
}
