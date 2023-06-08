package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.DishModel;

import java.util.List;
import java.util.Map;

public interface IDishServicePort {
    void saveDish(DishModel dishModel);

    DishModel updateDish(DishModel dishModel, Long id);

    DishModel findDishById(Long id);

    void changeDishStatus(DishModel dishModel, Long id);

    Map<String, List<DishModel>> findDishByRestaurant(Long restaurantId, int page, int size);
}
