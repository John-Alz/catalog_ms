package com.eatshub.catalog.infrastructure.entrypoints;

import com.eatshub.catalog.infrastructure.entrypoints.handler.RestaurantHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;


@Configuration
public class RestaurantRouterRest {


    private static RequestPredicate has(String name){
        return queryParam(name, v -> true);
    }

    @Bean
    public RouterFunction<ServerResponse> restaurantRouterFunction(RestaurantHandler handler){
        return RouterFunctions.route()
                .path("/restaurants", builder -> builder
                .GET( "/{name}", handler::getRestaurantByName)
                .GET(has("cuisineType"), handler::getRestaurantsByCuisineType)
                .GET(has("city"), handler::getRestaurantsByCity)
                .GET(has("priceType"), handler::getRestaurantsByPriceType)
                .GET(handler::getRestaurants)
                )
                .build();
    }

}
