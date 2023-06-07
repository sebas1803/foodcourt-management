package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class SaveOrderRequestDto {
    private Long idClient;
    private Long idRestaurant;
    private List<OrderItemRequestDto> orderDishes;
}
