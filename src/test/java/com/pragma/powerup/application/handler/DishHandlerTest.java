package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.handler.impl.DishHandler;
import com.pragma.powerup.application.mapper.request.IDishRequestMapper;
import com.pragma.powerup.application.mapper.response.IDishResponseMapper;
import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DishHandlerTest {

    @InjectMocks
    private DishHandler dishHandler;

    @Spy
    private IDishServicePort dishServicePort;
    @Spy
    private IDishRequestMapper dishRequestMapper;
    @Spy
    private IDishResponseMapper dishResponseMapper;

    @Test
    public void testSaveDish() {
        // Given
        SaveDishRequestDto saveDishRequestDto = new SaveDishRequestDto();
        Long restaurantId = 1L;
        saveDishRequestDto.setName("Arroz con pollo");
        saveDishRequestDto.setPrice(70.0);
        saveDishRequestDto.setDescription("Plato de arroz verde con pollo originario de Perú");
        saveDishRequestDto.setUrlImage("https://www.logo.dish.com");
        saveDishRequestDto.setCategory("FONDO");
        saveDishRequestDto.setRestaurant(restaurantId);

        DishModel dishModel = new DishModel();
        when(dishRequestMapper.toDishModel(saveDishRequestDto)).thenReturn(dishModel);

        // When
        dishHandler.saveDish(saveDishRequestDto);

        // Then
        verify(dishServicePort).saveDish(dishModel);
    }

    @Test
    public void testUpdateDish() {
        // Given
        DishModel dishId = new DishModel();
        dishId.setId(1L);
        UpdateDishRequestDto updateDishRequestDto = new UpdateDishRequestDto();
        updateDishRequestDto.setPrice(20.0);
        updateDishRequestDto.setDescription("New description");

        DishModel dishModel = new DishModel();
        when(dishServicePort.findDishById(dishId.getId())).thenReturn(dishModel);
        when(dishServicePort.updateDish(dishModel, dishId.getId())).thenReturn(dishModel);

        DishResponseDto expectedDishResponseDto = new DishResponseDto();
        expectedDishResponseDto.setName("Test Dish");
        expectedDishResponseDto.setPrice(20.0);
        expectedDishResponseDto.setDescription("New description");
        expectedDishResponseDto.setUrlImage("https://www.example.com/dish.jpg");
        expectedDishResponseDto.setCategory("BEBIDA");
        expectedDishResponseDto.setRestaurant(1L);

        when(dishResponseMapper.toResponse(dishModel)).thenReturn(expectedDishResponseDto);

        // When
        DishResponseDto updatedDish = dishHandler.updateDish(updateDishRequestDto, dishId.getId());

        // Then
        assertEquals(expectedDishResponseDto, updatedDish);
    }

    @Test
    public void testFindDishById() {
        // Given
        Long dishId = 1L;
        DishModel dishModel = new DishModel();
        dishModel.setId(dishId);
        dishModel.setName("Test Dish");
        dishModel.setPrice(10.0);
        dishModel.setDescription("Test description");
        dishModel.setUrlImage("https://www.example.com/dish.jpg");
        dishModel.setCategory("FONDO");
        dishModel.setRestaurant(new RestaurantModel());

        when(dishServicePort.findDishById(dishId)).thenReturn(dishModel);

        DishResponseDto expectedDishResponseDto = new DishResponseDto();
        expectedDishResponseDto.setName("Test Dish");
        expectedDishResponseDto.setPrice(10.0);
        expectedDishResponseDto.setDescription("Test description");
        expectedDishResponseDto.setUrlImage("https://www.example.com/dish.jpg");
        expectedDishResponseDto.setCategory("FONDO");
        expectedDishResponseDto.setRestaurant(dishModel.getRestaurant().getId());

        when(dishResponseMapper.toResponse(dishModel)).thenReturn(expectedDishResponseDto);

        // When
        DishResponseDto foundDish = dishHandler.findDishById(dishId);

        // Then
        assertEquals(expectedDishResponseDto, foundDish);
    }
}

