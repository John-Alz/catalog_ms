package com.eatshub.catalog.domain.gateways;

import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.domain.model.RestaurantModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RestaurantCatalogGateway {

    Flux<RestaurantModel> readAll(Long page, Long pageSize);
    Mono<RestaurantModel> readById(UUID id);
    Flux<RestaurantModel> readByCuisineType(String cuisineType);
    Mono<RestaurantModel> readByName(String name);
    Flux<RestaurantModel> readByPriceType(PriceType priceType);
    Flux<RestaurantModel> readByCity(String city);
    Mono<Boolean> existByCity(String city);


}
