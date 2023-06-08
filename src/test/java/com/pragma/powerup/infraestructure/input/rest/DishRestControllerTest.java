package com.pragma.powerup.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishStatusRequestDto;
import com.pragma.powerup.application.dto.response.DishListResponseDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import com.pragma.powerup.infrastructure.input.rest.DishRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DishRestControllerTest {

    @Mock
    private IDishHandler dishHandler;

    @InjectMocks
    private DishRestController dishRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dishRestController).build();
    }

    @Test
    void testCreateDish() throws Exception {
        // Given
        SaveDishRequestDto saveDishRequestDto = new SaveDishRequestDto();
        Long restaurantId = 1L;
        saveDishRequestDto.setName("Arroz con pollo");
        saveDishRequestDto.setPrice(70.0);
        saveDishRequestDto.setDescription("Plato de arroz verde con pollo originario de Perú");
        saveDishRequestDto.setUrlImage("https://www.logo.dish.com");
        saveDishRequestDto.setCategory("FONDO");
        saveDishRequestDto.setIdRestaurant(restaurantId);

        // When
        mockMvc.perform(post("/api/v1/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(saveDishRequestDto)))
                .andExpect(status().isCreated());

        // Then
        verify(dishHandler, times(1)).saveDish(any(SaveDishRequestDto.class));
    }

    @Test
    void testUpdateDish() throws Exception {
        // Given
        Long dishId = 1L;
        UpdateDishRequestDto updateDishRequestDto = new UpdateDishRequestDto();
        updateDishRequestDto.setPrice(20.0);
        updateDishRequestDto.setDescription("New description");

        DishResponseDto expectedDishResponseDto = new DishResponseDto();
        expectedDishResponseDto.setName("Test Dish");
        expectedDishResponseDto.setPrice(20.0);
        expectedDishResponseDto.setDescription("New description");
        expectedDishResponseDto.setUrlImage("https://www.example.com/dish.jpg");
        expectedDishResponseDto.setCategory("BEBIDA");
        expectedDishResponseDto.setIdRestaurant(1L);

        when(dishHandler.updateDish(any(UpdateDishRequestDto.class), eq(dishId))).thenReturn(expectedDishResponseDto);

        // When
        mockMvc.perform(put("/api/v1/dishes/" + dishId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(updateDishRequestDto)))
                .andExpect(status().isOk());

        // Then
        verify(dishHandler, times(1)).updateDish(any(UpdateDishRequestDto.class), eq(dishId));
    }

    @Test
    void testChangeDishStatus() throws Exception {
        // Given
        Long dishId = 1L;
        UpdateDishStatusRequestDto updateDishStatusRequestDto = new UpdateDishStatusRequestDto();
        updateDishStatusRequestDto.setStatus(1);

        // When
        mockMvc.perform(patch("/api/v1/dishes/" + dishId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDishStatusRequestDto)))
                .andExpect(status().isOk());

        // Then
        verify(dishHandler, times(1)).changeDishStatus(any(UpdateDishStatusRequestDto.class),
                eq(dishId));
    }

    @Test
    public void testGetDishesByRestaurantId() throws Exception {
        // Given
        Long restaurantId = 1L;
        int page = 0;
        int size = 10;
        Map<String, List<DishResponseDto>> expectedResponse = new HashMap<>();
        expectedResponse.put("FONDO", Arrays.asList(
                new DishResponseDto("dish1", 10.0, "desc1", "img1.com", "FONDO", 1L),
                new DishResponseDto("dish2", 10.0, "desc2", "img2.com", "FONDO", 1L)
        ));
        DishListResponseDto dishListResponseDto = new DishListResponseDto();
        dishListResponseDto.setDishesByCategory(expectedResponse);
        given(dishHandler.getDishesByRestaurant(restaurantId, page, size)).willReturn(dishListResponseDto);

        // When
        mockMvc.perform(get("/api/v1/dishes/restaurant/{restaurantId}?page={page}&size={size}", restaurantId, page, size)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then
        verify(dishHandler, times(1)).getDishesByRestaurant(restaurantId, page, size);
    }
}

