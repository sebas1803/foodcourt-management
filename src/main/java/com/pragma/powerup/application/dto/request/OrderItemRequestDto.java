package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long idOrder;
    private Long idDish;
    private int quantity;
}
