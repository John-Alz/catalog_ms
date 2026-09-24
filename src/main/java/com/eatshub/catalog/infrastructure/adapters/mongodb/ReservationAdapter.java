package com.eatshub.catalog.infrastructure.adapters.mongodb;

import com.eatshub.catalog.domain.enums.ReservationStatus;
import com.eatshub.catalog.domain.exceptions.ResourceNotFoundException;
import com.eatshub.catalog.domain.model.ReservationModel;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.ReservationCollection;
import com.eatshub.catalog.infrastructure.adapters.mongodb.mapper.ReservationAdapterMapper;
import com.eatshub.catalog.infrastructure.adapters.mongodb.repositories.ReservationRepository;
import com.eatshub.catalog.infrastructure.adapters.mongodb.repositories.RestaurantRepository;
import com.eatshub.catalog.domain.gateways.ReservationGateway;
import com.eatshub.catalog.infrastructure.adapters.mongodb.validators.BusinessValidator;
import com.eatshub.catalog.infrastructure.adapters.mongodb.validators.ReservationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationAdapter implements ReservationGateway {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationValidator reservationValidator;


    @Override
    public Mono<ReservationModel> createReservation(ReservationModel reservation) {
        List<BusinessValidator<ReservationCollection>> validations = List.of(
                reservationValidator.validateRestaurantNotClosed(),
                reservationValidator.validateAvailability()
        );
        ReservationCollection collection = ReservationAdapterMapper.MAPPER.toEntity(reservation);
        return reservationValidator.applyValidations(collection, validations)
                .then(validateExistRestaurant(reservation.getRestaurantId()).thenReturn(reservation))
                .map(this::applyDefaultStatus)
                .doOnNext(reservationItem -> log.info("Saving reservation for restaurant: {}", reservationItem.getRestaurantId()))
                .flatMap(item -> reservationRepository.save(ReservationAdapterMapper.MAPPER.toEntity(reservation)))
                .map(ReservationAdapterMapper.MAPPER::toModel)
                .doOnSuccess(savedReservation -> log.info("Reservation saved successfully with ID: " + savedReservation.getId()))
                .doOnError(error -> log.info("Error saving reservation: " + error.getMessage()));
    }

    @Override
    public Mono<ReservationModel> readByReservationId(UUID reservationId) {
        return validateExistReservation(reservationId)
                .map(ReservationAdapterMapper.MAPPER::toModel);
    }

    @Override
    public Flux<ReservationModel> readByRestaurantId(UUID restaurantId) {
        return validateExistRestaurant(restaurantId)
                .thenMany(reservationRepository.findByRestaurantId(restaurantId.toString()))
                .map(ReservationAdapterMapper.MAPPER::toModel)
                .doOnNext(reservation -> log.info("Found reservation for restaurant: " + restaurantId))
                .doOnError(error -> log.error("Error: " + error.getMessage()));
    }

    @Override
    public Flux<ReservationModel> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status) {
        return validateExistRestaurant(restaurantId)
                .thenMany(reservationRepository.findByRestaurantIdAndStatus(restaurantId, status))
                .map(ReservationAdapterMapper.MAPPER::toModel)
                .doOnNext(reservation -> log.info("Found reservation for restaurant: " + restaurantId + " with status: " + status))
                .doOnError(error -> log.error("Error: " + error.getMessage()));
    }

    @Override
    public Mono<ReservationModel> updateReservation(ReservationModel updateReservation, UUID reservationId) {
        List<BusinessValidator<ReservationCollection>> validations = List.of(
                reservationValidator.validateRestaurantNotClosed(),
                reservationValidator.validateAvailability()
        );
        ReservationCollection collection = ReservationAdapterMapper.MAPPER.toEntity(updateReservation);
        return validateExistReservation(reservationId)
                .flatMap(item -> reservationValidator.applyValidations(collection, validations).thenReturn(item))
                .flatMap(reservation -> {
                    reservation.setCustomerName(updateReservation.getCustomerName());
                    reservation.setTime(updateReservation.getTime());
                    reservation.setPartySize(updateReservation.getPartySize());
                    reservation.setStatus(updateReservation.getStatus());
                    reservation.setNotes(updateReservation.getNotes());
                    return reservationRepository.save(reservation)
                            .map(ReservationAdapterMapper.MAPPER::toModel)
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

    private ReservationModel applyDefaultStatus(ReservationModel reservation) {
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
