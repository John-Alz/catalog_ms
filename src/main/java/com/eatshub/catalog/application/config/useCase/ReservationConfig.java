package com.eatshub.catalog.application.config.useCase;

import com.eatshub.catalog.domain.gateways.ReservationGateway;
import com.eatshub.catalog.domain.gateways.RestaurantCatalogGateway;
import com.eatshub.catalog.domain.usecase.ReservationUseCase;
import com.eatshub.catalog.domain.validators.ReservationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ReservationConfig {

    @Bean
    public ReservationUseCase reservationUseCase(ReservationGateway reservationGateway, RestaurantCatalogGateway restaurantCatalogGateway, ReservationValidator reservationValidator) {
        return new ReservationUseCase(reservationGateway, restaurantCatalogGateway, reservationValidator);
    }

}
