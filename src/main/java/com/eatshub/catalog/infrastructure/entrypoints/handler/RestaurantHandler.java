package com.eatshub.catalog.infrastructure.entrypoints.handler;

import com.eatshub.catalog.domain.exceptions.BusinessException;
import com.eatshub.catalog.domain.usecase.RestaurantUseCase;
import com.eatshub.catalog.infrastructure.entrypoints.mapper.RestaurantMapper;
import com.eatshub.catalog.infrastructure.entrypoints.util.ErrorResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
@Slf4j
public class RestaurantHandler {

    private final RestaurantUseCase restaurantUseCase;

    public Mono<ServerResponse> getRestaurants(ServerRequest request) {
        final Long page = Long.parseLong(request.queryParam("page").orElse("0"));
        final Long pageSize = Long.parseLong(request.queryParam("pageSize").orElse("0"));
        return restaurantUseCase.readAll(page, pageSize)
                .transform(RestaurantMapper.MAPPER::toResponseFLux)
                .collectList()
                .flatMap(listRestaurant -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(listRestaurant))
                .doOnError(error -> log.error("Error consuming restaurants: ", error.getMessage()));
    }

    public Mono<ServerResponse> getRestaurantByName(ServerRequest request) {
        return restaurantUseCase.readByName(request.pathVariable("name"))
                .transform(RestaurantMapper.MAPPER::toResponseMono)
                .flatMap(listRestaurant -> ServerResponse.ok().bodyValue(listRestaurant))
                .doOnError(error -> log.error("Error consuming restaurant: ", error.getMessage()))
                .onErrorResume(error -> ErrorResponseFactory.build(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage()));
    }

    public Mono<ServerResponse> getRestaurantsByCuisineType(ServerRequest request) {
        String cuisineType = request.queryParam("cuisineType").orElse("");
        return restaurantUseCase.readByCuisineType(cuisineType)
                .transform(RestaurantMapper.MAPPER::toResponseFLux)
                .collectList()
                .flatMap(listRestaurant -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(listRestaurant))
                .doOnError(error -> log.error("Error consuming restaurants: ", error.getMessage()));
    }

    public Mono<ServerResponse> getRestaurantsByPriceType(ServerRequest request) {
        String priceType = request.queryParam("priceType").orElse("");
        return restaurantUseCase.readByPriceRange(priceType)
                .transform(RestaurantMapper.MAPPER::toResponseFLux)
                .collectList()
                .flatMap(listRestaurant -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(listRestaurant))
                .doOnError(error -> log.error("Error consuming restaurants: ", error.getMessage()));
    }

    public Mono<ServerResponse> getRestaurantsByCity(ServerRequest request) {
        String city = request.queryParam("city").orElse("");
        return restaurantUseCase.readByCity(city)
                .transform(RestaurantMapper.MAPPER::toResponseFLux)
                .collectList()
                .flatMap(listRestaurant -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(listRestaurant))
                .doOnError(error -> log.error("Error consuming restaurants: ", error.getMessage()))
                .onErrorResume(BusinessException.class, error -> ErrorResponseFactory.build(HttpStatus.BAD_REQUEST,
                        error.getMessage()))
                .onErrorResume(error -> ErrorResponseFactory.build(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage()));
    }


}
