package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.SaveDishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishRequestDto;
import com.pragma.powerup.application.dto.request.UpdateDishStatusRequestDto;
import com.pragma.powerup.application.dto.response.DishListResponseDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @Operation(summary = "Update a new dish")
    @ApiResponse(responseCode = "200", description = "Dish updated", content = @Content)
    @PutMapping("{id}")
    public ResponseEntity<DishResponseDto> updateDish(@RequestBody UpdateDishRequestDto updateDishRequestDto, @PathVariable Long id) {
        DishResponseDto updatedDish = dishHandler.updateDish(updateDishRequestDto, id);
        if (updatedDish == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedDish);
    }

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @Operation(summary = "Enable or disable a dish")
    @ApiResponse(responseCode = "200", description = "Dish status updated", content = @Content)
    @PatchMapping("{id}")
    public ResponseEntity<DishResponseDto> changeDishStatus(@RequestBody UpdateDishStatusRequestDto updateDishStatusRequestDto, @PathVariable Long id) {
        dishHandler.changeDishStatus(updateDishStatusRequestDto, id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @Operation(summary = "Get dishes by restaurant")
    @ApiResponse(responseCode = "200", description = "Dishes returned", content = @Content)
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<DishListResponseDto> getDishesByRestaurant(@PathVariable Long restaurantId, @RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(dishHandler.getDishesByRestaurant(restaurantId, page, size), HttpStatus.OK);
    }
}
