package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;

import java.util.List;

public interface IDishHandler {
    void saveDish(SaveDishRequestDto saveDishRequestDto);

    DishResponseDto updateDish(UpdateDishRequestDto updateDishRequestDto, Long id);

    DishResponseDto findDishById(Long id);

    //void changeDishStatus(UpdateDishStatusRequestDto updateDishStatusRequestDto, Long id);

    //List<DishListResponseDto> getDishesByRestaurant(Long restaurantId, int page, int size);
}
