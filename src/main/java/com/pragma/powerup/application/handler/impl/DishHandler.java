package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import com.pragma.powerup.application.mapper.request.IDishRequestMapper;
import com.pragma.powerup.application.mapper.response.IDishResponseMapper;
import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.DishModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public DishResponseDto findDishById(Long id) {
        DishModel dishModel = dishServicePort.findDishById(id);
        return dishResponseMapper.toResponse(dishModel);
    }
}
