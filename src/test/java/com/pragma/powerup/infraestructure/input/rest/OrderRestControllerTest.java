package com.pragma.powerup.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.AssignOrderRequestDto;
import com.pragma.powerup.application.dto.request.OrderItemRequestDto;
import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.infrastructure.input.rest.OrderRestController;
import com.pragma.powerup.infrastructure.security.jwt.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderRestControllerTest {

    @Mock
    private IOrderHandler orderHandler;

    @InjectMocks
    private OrderRestController orderRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController).build();
    }

    @Test
    void testSaveOrder() throws Exception {
        // Given
        SaveOrderRequestDto saveOrderRequestDto = new SaveOrderRequestDto();
        saveOrderRequestDto.setIdRestaurant(1L);
        saveOrderRequestDto.setIdClient(6L);

        List<OrderItemRequestDto> orderItems = new ArrayList<>();
        OrderItemRequestDto orderItem1 = new OrderItemRequestDto();
        orderItem1.setIdDish(1L);
        orderItem1.setQuantity(2);
        orderItems.add(orderItem1);

        OrderItemRequestDto orderItem2 = new OrderItemRequestDto();
        orderItem2.setIdDish(4L);
        orderItem2.setQuantity(4);
        orderItems.add(orderItem2);

        saveOrderRequestDto.setOrderDishes(orderItems);

        // Mock the behavior of orderHandler
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        when(orderHandler.saveOrder(saveOrderRequestDto)).thenReturn(orderResponseDto);

        // When
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saveOrderRequestDto)))
                .andExpect(status().isCreated());

        // Then
        verify(orderHandler, times(1)).saveOrder((any(SaveOrderRequestDto.class)));
    }

    @Test
    void testFindAllOrders() throws Exception {
        // Given
        String status = "PENDING";
        Long restaurantId = 1L;
        int page = 0;
        int size = 10;
        List<OrderResponseDto> orderResponseDtoList = Collections.singletonList(new OrderResponseDto());

        // When
        when(orderHandler.findAllOrdersByStatus(status, restaurantId, page, size)).thenReturn(orderResponseDtoList);

        mockMvc.perform(get("/api/v1/orders")
                        .param("status", status)
                        .param("restaurantId", restaurantId.toString())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        // Then
        verify(orderHandler, times(1)).findAllOrdersByStatus(status, restaurantId, page, size);
    }

    @Test
    public void testMarkOrderAsReady() throws Exception {
        // Given
        Long orderId = 1L;

        doNothing().when(orderHandler).markOrderAsReadyWithMessage(orderId);

        // When
        mockMvc.perform(put("/api/v1/orders/readyStatus/{orderId}", orderId)
                        .header("Authorization", "Bearer {your_token_here}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then
        verify(orderHandler, times(1)).markOrderAsReadyWithMessage(orderId);
    }
}