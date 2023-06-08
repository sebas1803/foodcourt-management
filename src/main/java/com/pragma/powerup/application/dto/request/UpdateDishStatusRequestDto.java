package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.DishModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDishStatusRequestDto {

    @Pattern(regexp = "^[01]$")
    private int status;

    public void updateStatus(DishModel dishModel) {
        if (status == 0 || status == 1) {
            dishModel.setStatus(status);
        } else {
            throw new DomainException("Invalid status value");
        }
    }
}
