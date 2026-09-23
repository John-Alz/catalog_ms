package com.eatshub.catalog.services.definitions;

import com.eatshub.catalog.enums.ReservationStatus;
import com.eatshub.catalog.model.ReservationCollection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationServiceDefinition {

    Mono<ReservationCollection> createReservation(ReservationCollection reservation);
    Mono<ReservationCollection> readByReservationId(UUID reservationId);
    Flux<ReservationCollection> readByRestaurantId(UUID restaurantId);
    Flux<ReservationCollection> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status);
    Mono<ReservationCollection> updateReservation(ReservationCollection reservation, UUID reservationId);
    Mono<Void> deleteReservation(UUID reservationId);

}
