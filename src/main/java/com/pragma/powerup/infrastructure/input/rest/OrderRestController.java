package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.AssignOrderRequestDto;
import com.pragma.powerup.application.dto.request.SaveOrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.infrastructure.security.config.SecurityContext;
import com.pragma.powerup.infrastructure.security.jwt.UserManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @Operation(summary = "Assign an order to an employee")
    @ApiResponse(responseCode = "200", description = "Orders assigned", content = @Content)
    @PutMapping("/assign")
    public ResponseEntity<String> updateAssignOrder(@RequestBody AssignOrderRequestDto ordersId) {
        UserManager userPrincipal = (UserManager) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderHandler.assignEmployeeToOrder(ordersId.getIdOrders(), userPrincipal.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @Operation(summary = "Mark order as ready")
    @ApiResponse(responseCode = "200", description = "Orders marked as ready", content = @Content)
    @PutMapping("/readyStatus/{orderId}")
    public ResponseEntity<Void> markOrderAsReady(@PathVariable Long orderId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        SecurityContext.setToken(token);
        orderHandler.markOrderAsReadyWithMessage(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
