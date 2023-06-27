package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        Long ownerId = restaurantModel.getIdOwner();
        //if (owner != null && owner.getRoles().contains("ROLE_OWNER")) {
        if (ownerId != null) {
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
    public RestaurantModel getRestaurantById(Long restaurantId) {
        return restaurantPersistencePort.getRestaurantById(restaurantId);
    }

    @Override
    public List<RestaurantModel> getAllRestaurants(int page, int size) {
        return restaurantPersistencePort.getAllRestaurants(page, size);
    }
}
