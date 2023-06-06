package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.api.UsersApiClient;
import com.pragma.powerup.infrastructure.security.config.SecurityContext;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final UsersApiClient usersApiClient;

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        String token = SecurityContext.getToken();
        Long ownerId = restaurantModel.getIdOwner();
        UserResponseDto owner = usersApiClient.getUserById(ownerId, token);
        if (owner != null && owner.getRoles().contains("ROLE_OWNER")) {
            restaurantPersistencePort.saveRestaurant(restaurantModel);
        } else {
            throw new DomainException("The user does not exist or have the proper role");
        }
    }

    @Override
    public List<RestaurantModel> getAllByNameIn(Collection<String> restaurantName) {
        return null;
    }

    @Override
    public RestaurantModel getById(Long id) {
        return null;
    }

    @Override
    public List<RestaurantModel> getAllRestaurants(int page, int size) {
        return restaurantPersistencePort.getAllRestaurants(page, size);
    }
}
