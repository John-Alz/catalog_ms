package com.eatshub.catalog.infrastructure.entrypoints;

import com.eatshub.catalog.infrastructure.entrypoints.handler.ReservationHandler;
import com.eatshub.catalog.infrastructure.entrypoints.handler.RestaurantHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ReservationRouterRest {

    @Bean
    public RouterFunction<ServerResponse> restaurantRouterFunction(ReservationHandler handler){
        return RouterFunctions.route()
                .POST("/reservation", handler::createReservation)
                .build();
    }
}
