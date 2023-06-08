package com.pragma.powerup.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.dto.request.SaveRestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantListResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.infrastructure.input.rest.RestaurantRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RestauranRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IRestaurantHandler restaurantHandler;

    @InjectMocks
    private RestaurantRestController restaurantRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantRestController).build();
    }

    @Test
    void testCreateRestaurant() throws Exception {
        // Given
        SaveRestaurantRequestDto requestDto = new SaveRestaurantRequestDto();
        requestDto.setName("Restaurant Name");
        requestDto.setNit("123456789");
        requestDto.setAddress("Restaurant Address");
        requestDto.setPhone("+123456782011");
        requestDto.setUrlLogo("http://www.restaurant.com/logo.png");
        requestDto.setIdOwner(1L);


        mockMvc.perform(post("/api/v1/restaurants")
                        .header("Authorization", "Bearer {your_token_here}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isCreated());

        // Then
        verify(restaurantHandler, times(1)).saveRestaurant((any(SaveRestaurantRequestDto.class)));
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        // Given
        int page = 0;
        int size = 10;
        List<RestaurantListResponseDto> expectedRestaurants = Arrays.asList(
                new RestaurantListResponseDto("Restaurant 1", "http://www.restaurant1.com/logo.png"),
                new RestaurantListResponseDto("Restaurant 2", "http://www.restaurant.com/logo.png")
        );

        given(restaurantHandler.getAllRestaurants(0, 10)).willReturn(expectedRestaurants);

        // When
        mockMvc.perform(get("/api/v1/restaurants?" + "page=" + page + "&size=" + size)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then
        verify(restaurantHandler, times(1)).getAllRestaurants(0, 10);
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
