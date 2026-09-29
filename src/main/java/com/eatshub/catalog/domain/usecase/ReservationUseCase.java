package com.eatshub.catalog.domain.usecase;

import com.eatshub.catalog.domain.enums.ReservationStatus;
import com.eatshub.catalog.domain.exceptions.ResourceNotFoundException;
import com.eatshub.catalog.domain.gateways.ReservationGateway;
import com.eatshub.catalog.domain.gateways.RestaurantCatalogGateway;
import com.eatshub.catalog.domain.model.ReservationModel;
import com.eatshub.catalog.domain.validators.BusinessValidator;
import com.eatshub.catalog.domain.validators.ReservationValidator;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.ReservationCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class ReservationUseCase {

    private final ReservationGateway reservationGateway;
    private final RestaurantCatalogGateway restaurantCatalogGateway;
    private final ReservationValidator reservationValidator;

    public Mono<ReservationModel> createReservation(ReservationModel reservationModel) {
        List<BusinessValidator<ReservationModel>> validations = List.of(
                reservationValidator.validateRestaurantNotClosed(),
                reservationValidator.validateAvailability()
        );
        return reservationValidator.applyValidations(reservationModel, validations)
                .then(validateExistRestaurant(reservationModel.getRestaurantId()).thenReturn(reservationModel))
                .map(this::applyDefaultStatus)
                .flatMap(item -> reservationGateway.createReservation(reservationModel));
    }

    public Mono<ReservationModel> readByReservationId(UUID reservationId) {
        return validateExistReservation(reservationId);
    }

    public Flux<ReservationModel> readByRestaurantId(UUID restaurantId) {
        return validateExistRestaurant(restaurantId)
                .thenMany(reservationGateway.readByRestaurantId(restaurantId));
    }

    public Flux<ReservationModel> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status) {
        return validateExistRestaurant(restaurantId)
                .thenMany(reservationGateway.readByRestaurantIdAndStatus(restaurantId, status));
    }

    public Mono<ReservationModel> updateReservation(ReservationModel reservationModelUpdate, UUID reservationId) {
        List<BusinessValidator<ReservationModel>> validations = List.of(
                reservationValidator.validateRestaurantNotClosed(),
                reservationValidator.validateAvailability()
        );
        return validateExistReservation(reservationId)
                .flatMap(reservation -> reservationValidator.applyValidations(reservationModelUpdate, validations).thenReturn(reservation))
                .flatMap(reservation -> {
                    reservation.setCustomerName(reservationModelUpdate.getCustomerName());
                    reservation.setTime(reservationModelUpdate.getTime());
                    reservation.setPartySize(reservationModelUpdate.getPartySize());
                    reservation.setStatus(reservationModelUpdate.getStatus());
                    reservation.setNotes(reservationModelUpdate.getNotes());
                    return reservationGateway.updateReservation(reservation, reservationId);
                });
    }

    public Mono<Void> deleteReservation(UUID reservationId) {
        return validateExistReservation(reservationId)
                .flatMap(reservation -> reservationGateway.deleteReservation(reservationId));
    }

    private ReservationModel applyDefaultStatus(ReservationModel reservation) {
        if (Objects.isNull(reservation.getStatus())) {
            reservation.setStatus(ReservationStatus.PENDING);
        }
        return reservation;
    }


    private Mono<Void> validateExistRestaurant(UUID restaurantId) {
        return restaurantCatalogGateway.readById(restaurantId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                .then();
    }

    private Mono<ReservationModel> validateExistReservation(UUID reservationId) {
        return reservationGateway.readByReservationId(reservationId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResourceNotFoundException("Reservation not found"))));
    }

}
