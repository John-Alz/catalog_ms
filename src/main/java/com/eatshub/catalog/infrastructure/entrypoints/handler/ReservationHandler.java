package com.eatshub.catalog.infrastructure.entrypoints.handler;

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
                .doOnError(throwable -> log.error("Error while creating reservation", throwable));
    }

}
