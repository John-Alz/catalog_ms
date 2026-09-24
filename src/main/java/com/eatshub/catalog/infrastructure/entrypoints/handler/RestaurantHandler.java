package com.eatshub.catalog.infrastructure.entrypoints.handler;

import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.domain.usecase.RestaurantUseCase;
import com.eatshub.catalog.infrastructure.entrypoints.dto.response.RestaurantResponse;
import com.eatshub.catalog.infrastructure.entrypoints.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RestaurantHandler {

    private final RestaurantUseCase restaurantUseCase;

    public Mono<ServerResponse> getRestaurants(ServerRequest request) {
        return restaurantUseCase.readAll()
                .transform(RestaurantMapper.MAPPER::toResponseFLux)
                .collectList()
                .flatMap(list -> ServerResponse.ok().bodyValue(list));
    }

}
