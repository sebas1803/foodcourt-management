package com.pragma.powerup.application.dto.response;

import com.pragma.powerup.domain.model.OrderItemModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Date date;
    private Long idClient;
    private Long idRestaurant;
    private Long idEmployee;
    private String status;
    private List<OrderItemModel> orderDishes;
}