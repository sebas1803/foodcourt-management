package com.pragma.powerup.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        saveDishRequestDto.setRestaurant(restaurantId);

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
        expectedDishResponseDto.setRestaurant(1L);

        when(dishHandler.updateDish(any(UpdateDishRequestDto.class), eq(dishId))).thenReturn(expectedDishResponseDto);

        // When
        mockMvc.perform(put("/api/v1/dishes/" + dishId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(updateDishRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedDishResponseDto.getName()))
                .andExpect(jsonPath("$.price").value(expectedDishResponseDto.getPrice()))
                .andExpect(jsonPath("$.description").value(expectedDishResponseDto.getDescription()))
                .andExpect(jsonPath("$.urlImage").value(expectedDishResponseDto.getUrlImage()))
                .andExpect(jsonPath("$.category").value(expectedDishResponseDto.getCategory()))
                .andExpect(jsonPath("$.restaurant").value(expectedDishResponseDto.getRestaurant()));

        // Then
        verify(dishHandler, times(1)).updateDish(any(UpdateDishRequestDto.class), eq(dishId));
    }
}

