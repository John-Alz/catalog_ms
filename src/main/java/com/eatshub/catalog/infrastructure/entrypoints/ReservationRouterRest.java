package com.eatshub.catalog.infrastructure.entrypoints;

import com.eatshub.catalog.infrastructure.entrypoints.handler.ReservationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;

@Configuration
public class ReservationRouterRest {

    private static RequestPredicate has(String name){
        return queryParam(name, v -> true);
    }


    @Bean
    public RouterFunction<ServerResponse> reservationRouterFunction(ReservationHandler handler){
        return RouterFunctions.route()
                .POST("/reservation", handler::createReservation)
                .GET("/reservation/{reservationId}", handler::getReservationById)
                .GET("/reservations", has("restaurantId").and(has("reservationStatus")), handler::readByRestaurantIdAndStatus)
                .GET("/reservations", has("restaurantId"), handler::getReservationsByRestaurant)
                .build();
    }
}
