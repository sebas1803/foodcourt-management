package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {
    private Long id;
    private Date date;
    private Long idClient;
    private Long idRestaurant;
    private Long idEmployee;
    private String status;
    private List<OrderItemModel> orderDishes;

    public static final String PENDING = "PENDING";
    public static final String IN_PREPARATION = "IN_PREPARATION";
    public static final String READY = "READY";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";
}
