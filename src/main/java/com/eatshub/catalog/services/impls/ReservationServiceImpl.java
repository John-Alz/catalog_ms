package com.eatshub.catalog.services.impls;

import com.eatshub.catalog.enums.ReservationStatus;
import com.eatshub.catalog.exceptions.ResourceNotFoundException;
import com.eatshub.catalog.model.ReservationCollection;
import com.eatshub.catalog.repositories.ReservationRepository;
import com.eatshub.catalog.repositories.RestaurantRepository;
import com.eatshub.catalog.services.definitions.ReservationServiceDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationServiceDefinition {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;


    @Override
    public Mono<ReservationCollection> createReservation(ReservationCollection reservation) {
        return validateExistRestaurant(reservation.getRestaurantId()).thenReturn(reservation)
                .map(this::applyDefaultStatus)
                .doOnNext(reservationItem -> log.info("Saving reservation for restaurant: {}", reservationItem.getRestaurantId()))
                .flatMap(reservationRepository::save)
                .doOnSuccess(savedReservation -> log.info("Reservation saved successfully with ID: " + savedReservation.getId()))
                .doOnError(error -> log.info("Error saving reservation: " + error.getMessage()));
    }

    @Override
    public Mono<ReservationCollection> readByReservationId(UUID reservationId) {
        return validateExistReservation(reservationId);
    }

    @Override
    public Flux<ReservationCollection> readByRestaurantId(UUID restaurantId) {
        return validateExistRestaurant(restaurantId)
                .thenMany(reservationRepository.findByRestaurantId(restaurantId.toString()))
                .doOnNext(reservation -> log.info("Found reservation for restaurant: " + restaurantId))
                .doOnError(error -> log.error("Error: " + error.getMessage()));
    }

    @Override
    public Flux<ReservationCollection> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status) {
        return validateExistRestaurant(restaurantId)
                .thenMany(reservationRepository.findByRestaurantIdAndStatus(restaurantId, status))
                .doOnNext(reservation -> log.info("Found reservation for restaurant: " + restaurantId + " with status: " + status))
                .doOnError(error -> log.error("Error: " + error.getMessage()));
    }

    @Override
    public Mono<ReservationCollection> updateReservation(ReservationCollection updateReservation, UUID reservationId) {
        return validateExistReservation(reservationId)
                .flatMap(reservation -> {
                    reservation.setCustomerName(updateReservation.getCustomerName());
                    reservation.setTime(updateReservation.getTime());
                    reservation.setPartySize(updateReservation.getPartySize());
                    reservation.setStatus(updateReservation.getStatus());
                    reservation.setNotes(updateReservation.getNotes());
                    return reservationRepository.save(reservation)
                            .doOnSuccess(updatedReservation -> log.info("Reservation updated successfully with ID: " + updatedReservation.getId()))
                            .doOnError(error -> log.error("Error updating reservation: " + error.getMessage()));
                });
    }

    @Override
    public Mono<Void> deleteReservation(UUID reservationId) {
        return validateExistReservation(reservationId)
                .doOnNext(reservation -> log.info("Deleting reservation with ID: " + reservationId))
                .flatMap(item -> reservationRepository.deleteById(reservationId))
                .doOnSuccess(sub -> log.info("Reservation deleted successfully with ID: " + reservationId))
                .doOnError(error -> log.error("Error deleting reservation: " + error.getMessage()));
    }

    private Mono<Void> validateExistRestaurant(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                .then();
    }

    private ReservationCollection applyDefaultStatus(ReservationCollection reservation) {
        if (Objects.isNull(reservation.getStatus())) {
            reservation.setStatus(ReservationStatus.PENDING);
        }
        return reservation;
    }

    private Mono<ReservationCollection> validateExistReservation(UUID reservationId) {
        return reservationRepository.findById(reservationId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found")));
    }
}
