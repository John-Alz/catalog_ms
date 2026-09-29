package com.eatshub.catalog.domain.gateways;

import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.domain.model.RestaurantModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RestaurantCatalogGateway {

    Flux<RestaurantModel> readAll();
    Mono<RestaurantModel> readById(UUID id);
    Flux<RestaurantModel> readByCuisineType(String cuisineType);
    Flux<RestaurantModel> readByName(String name);
    Flux<RestaurantModel> readByPriceType(PriceType priceType);
    Flux<RestaurantModel> readByCity(String city);

}
