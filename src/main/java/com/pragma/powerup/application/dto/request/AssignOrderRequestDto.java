package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class AssignOrderRequestDto {
    private List<Long> idOrders;
}
