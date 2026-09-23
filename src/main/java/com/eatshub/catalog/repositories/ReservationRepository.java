package com.eatshub.catalog.repositories;

import com.eatshub.catalog.enums.ReservationStatus;
import com.eatshub.catalog.model.ReservationCollection;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ReservationRepository extends ReactiveMongoRepository<ReservationCollection, UUID> {

    Flux<ReservationCollection> findByRestaurantId(String restaurantId);
    Flux<ReservationCollection> findByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status);

}
