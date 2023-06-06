package com.pragma.powerup.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        // Simulate token presence
        MockHttpServletRequestBuilder requestBuilder = post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestDto))
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4uYWxmYXJvQHByYWdtYS5jb20uY28iLCJpYXQiOjE2ODU5OTEyODIsImV4cCI6MTY4NjgzMTI4Mn0.pt7ITRclhH5de0S2AE13T8mHhm9nmogstvMTXrIW4W8");

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        verify(restaurantHandler).

                saveRestaurant(requestDto);

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Restaurant 1"))
                .andExpect(jsonPath("$[0].urlLogo").value("http://www.restaurant1.com/logo.png"))
                .andExpect(jsonPath("$[1].name").value("Restaurant 2"))
                .andExpect(jsonPath("$[1].urlLogo").value("http://www.restaurant.com/logo.png"));


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
