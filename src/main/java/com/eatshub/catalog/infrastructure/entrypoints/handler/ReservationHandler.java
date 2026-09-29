package com.eatshub.catalog.infrastructure.entrypoints.handler;

import com.eatshub.catalog.domain.enums.ReservationStatus;
import com.eatshub.catalog.domain.usecase.ReservationUseCase;
import com.eatshub.catalog.infrastructure.entrypoints.dto.request.ReservationRequest;
import com.eatshub.catalog.infrastructure.entrypoints.mapper.ReservationMapper;
import com.eatshub.catalog.infrastructure.entrypoints.util.RequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationHandler {

    private final ReservationUseCase reservationUseCase;
    private final ReservationMapper reservationMapper;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> createReservation(ServerRequest request) {
        return request.bodyToMono(ReservationRequest.class)
                .flatMap(requestValidator::validate)
                .transform(reservationMapper::toRequestMono)
                .flatMap(reservationUseCase::createReservation)
                .transform(reservationMapper::toResponseMono)
                .flatMap(reservationResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reservationResponse))
                .doOnSuccess(response -> log.info("Reservation created successfully"))
                .doOnError(throwable -> log.error("Error while creating reservation", throwable.getMessage()));
    }

    public Mono<ServerResponse> getReservationById(ServerRequest request) {
        return reservationUseCase.readByReservationId(UUID.fromString(request.pathVariable("reservationId")))
                .map(reservationMapper::toResponse)
                .flatMap(reservationResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reservationResponse)
                )
                .doOnSuccess(response -> log.info("Get reservation successfully"))
                .doOnError(throwable -> log.error("Error while getting reservation", throwable.getMessage()));
    }

    public Mono<ServerResponse> getReservationsByRestaurant(ServerRequest request) {
        String restaurantId = request.queryParam("restaurantId").orElse("");
        return reservationUseCase.readByRestaurantId(UUID.fromString(restaurantId))
                .transform(reservationMapper::toResponseFlux)
                .collectList()
                .flatMap(reservationResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reservationResponse))
                .doOnSuccess(response -> log.info("Get reservations successfully"))
                .doOnError(throwable -> log.error("Error while getting reservations", throwable.getMessage()));
    }

    public Mono<ServerResponse> readByRestaurantIdAndStatus(ServerRequest request) {
        String restaurantId = request.queryParam("restaurantId").orElse("");
        String reservationStatus = request.queryParam("reservationStatus").orElse("");
        return reservationUseCase.readByRestaurantIdAndStatus(UUID.fromString(restaurantId), ReservationStatus.valueOf(reservationStatus))
                .transform(reservationMapper::toResponseFlux)
                .collectList()
                .flatMap(reservationResponses -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reservationResponses))
                .doOnSuccess(response -> log.info("Get reservations successfully"))
                .doOnError(throwable -> log.error("Error while getting reservations", throwable.getMessage()));
    }

    public Mono<ServerResponse> updateReservation(ServerRequest request) {
        return request.bodyToMono(ReservationRequest.class)
                .transform(reservationMapper::toRequestMono)
                .flatMap(reservationRequest -> reservationUseCase.updateReservation(reservationRequest, UUID.fromString(request.pathVariable("reservationId"))))
                .transform(reservationMapper::toResponseMono)
                .flatMap(reservationResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reservationResponse))
                .doOnSuccess(response -> log.info("Update reservation successfully"))
                .doOnError(throwable -> log.error("Error while updating reservation", throwable.getMessage()));
    }

    public Mono<ServerResponse> deleteReservation(ServerRequest request) {
        return reservationUseCase.deleteReservation(UUID.fromString(request.pathVariable("reservationId")))
                .then(ServerResponse.noContent().build())
                .doOnSuccess(response -> log.info("Delete reservation successfully"))
                .doOnError(throwable -> log.error("Error while deleting reservation", throwable.getMessage()));
    }

}
