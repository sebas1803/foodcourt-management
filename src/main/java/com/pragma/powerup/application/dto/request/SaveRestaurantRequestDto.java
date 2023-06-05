package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
public class SaveRestaurantRequestDto {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9\\s]+$", message = "Invalid name")
    private String name;

    @NotBlank(message = "NIT is required")
    @Pattern(regexp = "^[0-9]+$", message = "Invalid NIT")
    private String nit;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone is required")
    @Size(max = 13, message = "Phone number must have a maximum of 13 characters")
    @Pattern(regexp = "\\+?[0-9]+", message = "Phone number must contain only digits and can have '+' symbol")
    private String phone;

    @NotBlank(message = "Logo url is required")
    private String urlLogo;

    @NotNull(message = "Owner id is required")
    private Long idOwner;
}
