package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.handler.IOrderHandler;
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

@RestController
@Tag(name = "Orders")
@RequestMapping("api/v1/orders")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class OrderRestController {
    private final IOrderHandler orderHandler;

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @Operation(summary = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created", content = @Content)
    @PostMapping()
    public ResponseEntity<Void> saveOrder(@RequestBody SaveOrderRequestDto saveOrderRequestDto) {
        orderHandler.saveOrder(saveOrderRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
