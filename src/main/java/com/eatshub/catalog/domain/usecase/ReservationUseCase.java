package com.eatshub.catalog.domain.usecase;

import com.eatshub.catalog.domain.gateways.ReservationGateway;
import com.eatshub.catalog.domain.model.ReservationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservationUseCase {

    private final ReservationGateway reservationGateway;

    public Mono<ReservationModel> createReservation(ReservationModel reservationModel) {
        return reservationGateway.createReservation(reservationModel);
    }

}
