package com.pragma.powerup.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}

