package com.pragma.powerup.application.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishResponseDto {
    private String name;
    private Double price;
    private String description;
    private String urlImage;
    private String category;
    private Long idRestaurant;
}
