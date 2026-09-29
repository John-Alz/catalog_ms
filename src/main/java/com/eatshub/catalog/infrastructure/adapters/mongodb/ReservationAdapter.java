package com.eatshub.catalog.infrastructure.adapters.mongodb;

import com.eatshub.catalog.domain.enums.ReservationStatus;
import com.eatshub.catalog.domain.model.ReservationModel;
import com.eatshub.catalog.infrastructure.adapters.mongodb.mapper.ReservationAdapterMapper;
import com.eatshub.catalog.infrastructure.adapters.mongodb.repositories.ReservationRepository;
import com.eatshub.catalog.domain.gateways.ReservationGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationAdapter implements ReservationGateway {

    private final ReservationRepository reservationRepository;

    @Override
    public Mono<ReservationModel> createReservation(ReservationModel reservation) {
        return reservationRepository.save(ReservationAdapterMapper.MAPPER.toEntity(reservation))
                .doOnNext(reservationItem -> log.info("Saving reservation for restaurant: {}", reservationItem.getRestaurantId()))
                .map(ReservationAdapterMapper.MAPPER::toModel)
                .doOnSuccess(savedReservation -> log.info("Reservation saved successfully with ID: " + savedReservation.getId()))
                .doOnError(error -> log.info("Error saving reservation: " + error.getMessage()));
    }

    @Override
    public Mono<ReservationModel> readByReservationId(UUID reservationId) {
        return reservationRepository.findById(reservationId)
                .map(ReservationAdapterMapper.MAPPER::toModel);
    }

    @Override
    public Flux<ReservationModel> readByRestaurantId(UUID restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId.toString())
                .map(ReservationAdapterMapper.MAPPER::toModel)
                .doOnNext(reservation -> log.info("Found reservation for restaurant: " + restaurantId))
                .doOnError(error -> log.error("Error: " + error.getMessage()));
    }

    @Override
    public Flux<ReservationModel> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status) {
        return reservationRepository.findByRestaurantIdAndStatus(restaurantId, status)
                .map(ReservationAdapterMapper.MAPPER::toModel)
                .doOnNext(reservation -> log.info("Found reservation for restaurant: " + restaurantId + " with status: " + status))
                .doOnError(error -> log.error("Error: " + error.getMessage()));
    }

    @Override
    public Mono<ReservationModel> updateReservation(ReservationModel updateReservation, UUID reservationId) {
        return reservationRepository.save(ReservationAdapterMapper.MAPPER.toEntity(updateReservation))
                .map(ReservationAdapterMapper.MAPPER::toModel)
                .doOnSuccess(updatedReservation -> log.info("Reservation updated successfully with ID: " + updatedReservation.getId()))
                .doOnError(error -> log.error("Error updating reservation: " + error.getMessage()));
    }

    @Override
    public Mono<Void> deleteReservation(UUID reservationId) {
        return reservationRepository.deleteById(reservationId)
                .doOnNext(reservation -> log.info("Deleting reservation with ID: " + reservationId))
                .doOnSuccess(sub -> log.info("Reservation deleted successfully with ID: " + reservationId))
                .doOnError(error -> log.error("Error deleting reservation: " + error.getMessage()));
    }
}
