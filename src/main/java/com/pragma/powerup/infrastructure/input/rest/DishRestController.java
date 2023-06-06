package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
import com.pragma.powerup.application.dto.request.SaveRestaurantRequestDto;
import com.pragma.powerup.application.handler.IDishHandler;
import com.pragma.powerup.infrastructure.security.config.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Tag(name = "Dishes")
@RequestMapping("api/v1/dishes")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DishRestController {
    private final IDishHandler dishHandler;

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @Operation(summary = "Create a new dish")
    @ApiResponse(responseCode = "201", description = "Dish created", content = @Content)
    @PostMapping()
    public ResponseEntity<Void> createDish(@RequestBody SaveDishRequestDto saveDishRequestDto) {
        dishHandler.saveDish(saveDishRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
