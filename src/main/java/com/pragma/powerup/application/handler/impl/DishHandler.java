package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishStatusRequestDto;
import com.pragma.powerup.application.dto.response.DishListResponseDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import com.pragma.powerup.application.mapper.request.IDishRequestMapper;
import com.pragma.powerup.application.mapper.response.IDishResponseMapper;
import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.DishModel;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {
    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public void saveDish(SaveDishRequestDto saveDishRequestDto) {
        dishServicePort.saveDish(dishRequestMapper.toDishModel(saveDishRequestDto));
    }

    @Override
    public DishResponseDto updateDish(UpdateDishRequestDto updateDishRequestDto, Long id) {
        DishModel dishModel = dishServicePort.findDishById(id);
        updateDishRequestDto.update(dishModel);
        DishModel updatedDish = dishServicePort.updateDish(dishModel, id);
        return dishResponseMapper.toResponse(updatedDish);
    }

    @Override
    public DishResponseDto findDishById(Long id) {
        DishModel dishModel = dishServicePort.findDishById(id);
        return dishResponseMapper.toResponse(dishModel);
    }

    @Override
    public void changeDishStatus(UpdateDishStatusRequestDto updateDishStatusRequestDto, Long id) {
        if (updateDishStatusRequestDto.getStatus() != 0 && updateDishStatusRequestDto.getStatus() != 1) {
            throw new DomainException("Invalid status value. It must be either 0 or 1.");
        }

        DishModel dishById = dishServicePort.findDishById(id);
        if (dishById == null) {
            throw new DomainException("Dish not found");
        }

        updateDishStatusRequestDto.updateStatus(dishById);
        dishServicePort.changeDishStatus(dishById, id);
    }

    @Override
    public DishListResponseDto getDishesByRestaurant(Long restaurantId, int page, int size) {
        Map<String, List<DishModel>> dishes = dishServicePort.findDishByRestaurant(restaurantId, page, size);
        DishListResponseDto dishListResponse = new DishListResponseDto();
        Map<String, List<DishResponseDto>> dishesByCategory = new HashMap<>();

        for (Map.Entry<String, List<DishModel>> entry : dishes.entrySet()) {
            String category = entry.getKey();
            List<DishModel> dishModels = entry.getValue();
            List<DishResponseDto> dishResponses = dishResponseMapper.toListResponse(dishModels);
            dishesByCategory.put(category, dishResponses);
        }

        dishListResponse.setDishesByCategory(dishesByCategory);
        return dishListResponse;
    }
}
