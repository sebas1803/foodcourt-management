package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.RestaurantModel;

import java.util.Collection;
import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(RestaurantModel restaurantModel);

    List<RestaurantModel> getAllByNameIn(Collection<String> restaurantName);

    List<RestaurantModel> getAllRestaurants(int page, int size);

    RestaurantModel getRestaurantById(Long restaurantId);
}
