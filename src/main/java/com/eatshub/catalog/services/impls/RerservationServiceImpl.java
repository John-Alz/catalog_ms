package com.eatshub.catalog.services.impls;

import com.eatshub.catalog.enums.ReservationStatus;
import com.eatshub.catalog.model.ReservationCollection;
import com.eatshub.catalog.repositories.ReservationRepository;
import com.eatshub.catalog.repositories.RestaurantRepository;
import com.eatshub.catalog.services.definitions.ReservationServiceDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RerservationServiceImpl implements ReservationServiceDefinition {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public Mono<ReservationCollection> createReservation(ReservationCollection reservation) {
        return null;
    }

    @Override
    public Flux<ReservationCollection> readByRestaurantId(String restaurantId) {
        return null;
    }

    @Override
    public Flux<ReservationCollection> readByRestaurantIdAndStatus(String restaurantId, ReservationStatus status) {
        return null;
    }

    @Override
    public Mono<ReservationCollection> updateReservation(ReservationCollection reservation, UUID reservationId) {
        return null;
    }

    @Override
    public Mono<Void> deleteReservation(UUID reservationId) {
        return null;
    }
}
