package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.SaveRestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantListResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<Void> createRestaurant(@Valid @RequestBody SaveRestaurantRequestDto saveRestaurantRequestDto) {
        restaurantHandler.saveRestaurant(saveRestaurantRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //@PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @Operation(summary = "Get all restaurants")
    @ApiResponse(responseCode = "200", description = "All restaurants returned", content = @Content)
    @GetMapping()
    public ResponseEntity<List<RestaurantListResponseDto>> getAllRestaurant(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(restaurantHandler.getAllRestaurants(page, size), HttpStatus.OK);
    }
}
