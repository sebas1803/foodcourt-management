package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @Operation(summary = "Find all orders by status")
    @ApiResponse(responseCode = "200", description = "Orders by status returned", content = @Content)
    @GetMapping()
    public ResponseEntity<List<OrderResponseDto>> findAllOrders(@RequestParam String status,
                                                                @RequestParam Long restaurantId,
                                                                @RequestParam int page,
                                                                @RequestParam int size) {
        List<OrderResponseDto> orderResponseDtoList = orderHandler.findAllOrdersByStatus(status, restaurantId, page, size);
        return new ResponseEntity<>(orderResponseDtoList, HttpStatus.OK);
    }
}
