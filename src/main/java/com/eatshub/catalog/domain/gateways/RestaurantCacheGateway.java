package com.eatshub.catalog.domain.gateways;

import com.eatshub.catalog.domain.model.RestaurantModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RestaurantCacheGateway {

    Mono<RestaurantModel> saveCacheRestaurant(String key, RestaurantModel restaurant);
    Mono<RestaurantModel> getCacheRestaurant(String key);
    Flux<RestaurantModel> getCacheRestaurants(String key);
    Flux<RestaurantModel> saveCacheRestaurants(String key, List<RestaurantModel> restaurants);

}
