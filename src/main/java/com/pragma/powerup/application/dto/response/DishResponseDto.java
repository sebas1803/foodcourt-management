package com.pragma.powerup.application.dto.response;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class DishResponseDto {
    private String name;
    private Double price;
    private String description;
    private String urlImage;
    private String category;
    private Long restaurant;
}
