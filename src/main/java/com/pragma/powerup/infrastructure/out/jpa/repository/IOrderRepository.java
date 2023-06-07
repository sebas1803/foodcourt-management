package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByIdClient(Long idClient);

    List<OrderEntity> findAllByStatusInAndIdClient(List<String> status, Long idClient);

    Page<OrderEntity> findAllByIdRestaurantAndStatus(Pageable pageable, Long idRestaurant, String status);

}
