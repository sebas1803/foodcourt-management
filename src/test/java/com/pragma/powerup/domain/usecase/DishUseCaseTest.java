package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DishUseCaseTest {

    @Spy
    private IDishPersistencePort dishPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    @BeforeEach
    public void setup() {
        dishUseCase = new DishUseCase(dishPersistencePort);
    }

    @Test
    public void testSaveDish() {
        // Given
        RestaurantModel restaurantId = new RestaurantModel();
        restaurantId.setId(1L);
        DishModel dishModel = new DishModel();
        dishModel.setName("Pizza");
        dishModel.setPrice(50.0);
        dishModel.setDescription("Pizza de pepperoni");
        dishModel.setUrlImage("https://www.logo.dish.com");
        dishModel.setCategory("FONDO");
        dishModel.setRestaurant(restaurantId);

        // When
        dishUseCase.saveDish(dishModel);

        // Then
        verify(dishPersistencePort).saveDish(dishModel);
    }

    @Test
    public void testFindDishById() {
        // Given
        Long id = 1L;
        DishModel expectedDishModel = new DishModel();
        expectedDishModel.setId(id);
        expectedDishModel.setName("Test Dish");
        expectedDishModel.setPrice(10.0);
        expectedDishModel.setDescription("Test Description");
        expectedDishModel.setUrlImage("https://www.example.com/dish.jpg");
        expectedDishModel.setCategory("FONDO");
        expectedDishModel.setRestaurant(new RestaurantModel());

        when(dishPersistencePort.findDishById(id)).thenReturn(expectedDishModel);

        // When
        DishModel actualDishModel = dishUseCase.findDishById(id);

        // Then
        assertEquals(expectedDishModel, actualDishModel);
    }
}
