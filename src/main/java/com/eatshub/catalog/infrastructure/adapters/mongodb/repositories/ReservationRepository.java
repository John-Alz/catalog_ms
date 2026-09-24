package com.eatshub.catalog.infrastructure.adapters.mongodb.repositories;

import com.eatshub.catalog.domain.enums.ReservationStatus;

import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.ReservationCollection;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ReservationRepository extends ReactiveMongoRepository<ReservationCollection, UUID> {

    Flux<ReservationCollection> findByRestaurantId(String restaurantId);
    Flux<ReservationCollection> findByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status);

}
