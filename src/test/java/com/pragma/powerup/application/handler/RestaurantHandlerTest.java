package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.SaveRestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.impl.RestaurantHandler;
import com.pragma.powerup.application.mapper.request.IRestaurantRequestMapper;
import com.pragma.powerup.application.mapper.response.IRestaurantResponseMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RestaurantHandlerTest {
    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Spy
    private IRestaurantServicePort restaurantServicePort;

    @Spy
    private IRestaurantRequestMapper restaurantRequestMapper;

    @Spy
    private IRestaurantResponseMapper restaurantResponseMapper;

    @Test
    public void testSaveRestaurant() {
        // Given
        SaveRestaurantRequestDto requestDto = new SaveRestaurantRequestDto();
        RestaurantModel expectedModel = new RestaurantModel();
        when(restaurantRequestMapper.toRestaurantModel(requestDto)).thenReturn(expectedModel);

        // When
        restaurantHandler.saveRestaurant(requestDto);

        // Then
        ArgumentCaptor<RestaurantModel> captor = ArgumentCaptor.forClass(RestaurantModel.class);
        verify(restaurantServicePort).saveRestaurant(captor.capture());
        RestaurantModel actualModel = captor.getValue();
        assertEquals(expectedModel, actualModel);
    }
}
