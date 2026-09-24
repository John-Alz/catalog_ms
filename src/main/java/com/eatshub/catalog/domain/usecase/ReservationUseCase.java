package com.eatshub.catalog.domain.usecase;

import com.eatshub.catalog.domain.enums.ReservationStatus;
import com.eatshub.catalog.domain.gateways.ReservationGateway;
import com.eatshub.catalog.domain.model.ReservationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class ReservationUseCase {

    private final ReservationGateway reservationGateway;

    public Mono<ReservationModel> createReservation(ReservationModel reservationModel) {
        return reservationGateway.createReservation(reservationModel);
    }

    public Mono<ReservationModel> readByReservationId(UUID reservationId) {
        return reservationGateway.readByReservationId(reservationId);
    }

    public Flux<ReservationModel> readByRestaurantId(UUID restaurantId) {
        return reservationGateway.readByRestaurantId(restaurantId);
    }

    public Flux<ReservationModel> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status) {
        return  reservationGateway.readByRestaurantIdAndStatus(restaurantId, status);
    }

    public Mono<ReservationModel> updateReservation(ReservationModel reservationModel, UUID reservationId) {
        return reservationGateway.updateReservation(reservationModel, reservationId);
    }

    public Mono<Void> deleteReservation(UUID reservationId) {
        return reservationGateway.deleteReservation(reservationId);
    }

}
