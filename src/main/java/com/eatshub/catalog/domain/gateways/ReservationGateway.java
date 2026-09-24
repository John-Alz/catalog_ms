package com.eatshub.catalog.domain.gateways;

import com.eatshub.catalog.domain.enums.ReservationStatus;
import com.eatshub.catalog.domain.model.ReservationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationGateway {

    Mono<ReservationModel> createReservation(ReservationModel reservation);
    Mono<ReservationModel> readByReservationId(UUID reservationId);
    Flux<ReservationModel> readByRestaurantId(UUID restaurantId);
    Flux<ReservationModel> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status);
    Mono<ReservationModel> updateReservation(ReservationModel reservation, UUID reservationId);
    Mono<Void> deleteReservation(UUID reservationId);

}
