package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@RequiredArgsConstructor
public class SaveDishRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Price must be a positive number and not zero")
    private Double price;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "URL image is required")
    private String urlImage;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Restaurant is required")
    private Long idRestaurant;
}

