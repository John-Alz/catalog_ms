package com.eatshub.catalog.infrastructure.entrypoints;

import com.eatshub.catalog.infrastructure.entrypoints.handler.RestaurantHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RestaurantRouterRest {

    private static final String BASE_URL = "/api/v1";

    @Bean
    public RouterFunction<ServerResponse> restaurantRouterFunction(RestaurantHandler handler){
        return RouterFunctions.route()
                .GET(BASE_URL + "/restaurants", handler::getRestaurants)
                .build();
    }

}
