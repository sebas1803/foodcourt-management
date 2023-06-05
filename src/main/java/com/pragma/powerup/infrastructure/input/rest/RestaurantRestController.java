package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.SaveRestaurantRequestDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.infrastructure.security.config.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Restaurants")
@RequestMapping("api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantRestController {
    private final IRestaurantHandler restaurantHandler;

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Create a new restaurant")
    @ApiResponse(responseCode = "201", description = "Restaurant created", content = @Content)
    @PostMapping()
    public ResponseEntity<Void> createRestaurant(@Valid @RequestBody SaveRestaurantRequestDto saveRestaurantRequestDto,
                                                 @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        SecurityContext.setToken(token);
        restaurantHandler.saveRestaurant(saveRestaurantRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
