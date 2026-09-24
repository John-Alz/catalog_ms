package com.eatshub.catalog.application.config.useCase;

import com.eatshub.catalog.domain.gateways.RestaurantCatalogGateway;
import com.eatshub.catalog.domain.usecase.RestaurantUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RestaurantConfig {

    @Bean
    public RestaurantUseCase restaurantUseCase(RestaurantCatalogGateway restaurantCatalogGateway) {
        return new RestaurantUseCase(restaurantCatalogGateway);
    }

}

