package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.domain.model.DishModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@RequiredArgsConstructor
public class UpdateDishRequestDto {

    @NotNull(message = "Price is required")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Price must be a positive number and not zero")
    private Double price;

    @NotBlank(message = "Description is required")
    private String description;

    public void update(DishModel dishModel) {
        if (price != null) {
            dishModel.setPrice(price);
        }
        if (description != null) {
            dishModel.setDescription(description);
        }
    }
}
