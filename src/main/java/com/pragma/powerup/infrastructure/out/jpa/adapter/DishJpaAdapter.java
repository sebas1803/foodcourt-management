package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IDishRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {
    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final IRestaurantRepository restaurantRepository;

    @Override
    public void saveDish(DishModel dishModel) {
        DishEntity dishEntity = dishRepository.save(dishEntityMapper.toEntityDish(dishModel));
        dishEntityMapper.toDishModel(dishEntity);
    }

    @Override
    public DishModel updateDish(DishModel dishModel, Long id) {
        Optional<DishEntity> optionalDishEntity = dishRepository.findById(id);
        if (optionalDishEntity.isPresent()) {
            DishEntity dishEntity = optionalDishEntity.get();
            if (dishModel.getDescription() != null) {
                dishEntity.setDescription(dishModel.getDescription());
            }
            if (dishModel.getPrice() != null) {
                dishEntity.setPrice(dishModel.getPrice());
            }
            dishRepository.save(dishEntity);
            return dishEntityMapper.toDishModel(dishEntity);
        } else {
            throw new NoDataFoundException();
        }
    }

    @Override
    public DishModel findDishById(Long id) {
        return dishEntityMapper.toDishModel(dishRepository.findById(id).orElseThrow(NoDataFoundException::new));
    }

    @Override
    public void changeDishStatus(DishModel dishModel, Long id) {
        Optional<DishEntity> optionalDishEntity = dishRepository.findById(id);
        if (optionalDishEntity.isPresent()) {
            DishEntity dishEntity = optionalDishEntity.get();
            if (dishModel.getStatus() == 0 || dishModel.getStatus() == 1) {
                dishEntity.setStatus(dishModel.getStatus());
            } else {
                throw new NoDataFoundException();
            }
            dishRepository.save(dishEntity);
        } else {
            throw new NoDataFoundException();
        }
    }

    @Override
    public Map<String, List<DishModel>> findDishByRestaurant(Long restaurantId, int page, int size) {
        Optional<RestaurantEntity> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isEmpty()) {
            throw new NoDataFoundException();
        }

        Page<DishEntity> dishPage = dishRepository.findAllByRestaurantId(PageRequest.of(page, size), restaurantId);
        List<DishEntity> dishEntities = dishPage.getContent();

        Map<String, List<DishModel>> dishesByCategoryModel = dishEntities.stream()
                .collect(Collectors.groupingBy(DishEntity::getCategory,
                        Collectors.mapping(dishEntityMapper::toDishModel, Collectors.toList())));

        return dishesByCategoryModel;
    }
}
