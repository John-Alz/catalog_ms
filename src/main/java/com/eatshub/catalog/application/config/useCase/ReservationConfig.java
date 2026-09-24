package com.eatshub.catalog.application.config.useCase;

import com.eatshub.catalog.domain.gateways.ReservationGateway;
import com.eatshub.catalog.domain.usecase.ReservationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ReservationConfig {

    @Bean
    public ReservationUseCase reservationUseCase(ReservationGateway reservationGateway) {
        return new ReservationUseCase(reservationGateway);
    }

}
