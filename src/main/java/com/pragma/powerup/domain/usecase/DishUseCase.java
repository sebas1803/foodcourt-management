package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DishUseCase implements IDishServicePort {
    private final IDishPersistencePort dishPersistencePort;

    @Override
    public void saveDish(DishModel dishModel) {
        dishModel.setStatus(1);
        dishPersistencePort.saveDish(dishModel);
    }

    @Override
    public DishModel updateDish(DishModel dishModel, Long id) {
        DishModel existingDishModel = dishPersistencePort.findDishById(id);
        if (existingDishModel == null) {
            throw new DomainException("Dish not found");
        }
        if (dishModel.getDescription() != null) {
            existingDishModel.setDescription(dishModel.getDescription());
        }
        if (dishModel.getPrice() != null) {
            existingDishModel.setPrice(dishModel.getPrice());
        }
        return dishPersistencePort.updateDish(existingDishModel, id);
    }

    @Override
    public DishModel findDishById(Long id) {
        return dishPersistencePort.findDishById(id);
    }

    @Override
    public void changeDishStatus(DishModel dishModel, Long id) {
        DishModel existingDishModel = dishPersistencePort.findDishById(id);
        if (existingDishModel == null) {
            throw new DomainException("Dish not found");
        }
        if (dishModel.getStatus() == 0 || dishModel.getStatus() == 1) {
            existingDishModel.setStatus(dishModel.getStatus());
        } else {
            throw new DomainException("Status not valid");
        }
        dishPersistencePort.changeDishStatus(existingDishModel, id);
    }

    @Override
    public Map<String, List<DishModel>> findDishByRestaurant(Long restaurantId, int page, int size) {
        return dishPersistencePort.findDishByRestaurant(restaurantId, page, size);
    }
}
