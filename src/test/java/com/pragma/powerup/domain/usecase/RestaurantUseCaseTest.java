package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.api.UsersApiClient;
import com.pragma.powerup.infrastructure.security.config.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveRestaurant() {
        // Given
        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setIdOwner(1L);

        UserResponseDto owner = new UserResponseDto();
        owner.setRoles(Collections.singletonList("ROLE_OWNER"));

        UsersApiClient usersApiClientMock = Mockito.mock(UsersApiClient.class);
        Mockito.when(usersApiClientMock.getUserById(1L, SecurityContext.getToken())).thenReturn(owner);

        RestaurantUseCase restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, usersApiClientMock);

        // When
        restaurantUseCase.saveRestaurant(restaurantModel);

        // Then
        verify(restaurantPersistencePort).saveRestaurant(restaurantModel);
    }

}



