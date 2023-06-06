package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        RestaurantEntity restaurantEntity = restaurantRepository.save(restaurantEntityMapper.toEntityRestaurant(restaurantModel));
        restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public List<RestaurantModel> getAllRestaurants(int page, int size) {
        Page<RestaurantEntity> restaurantPage = restaurantRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
        List<RestaurantEntity> restaurantEntities = restaurantPage.getContent();
        return restaurantEntityMapper.toRestaurantModelList(restaurantEntities);
    }
}
