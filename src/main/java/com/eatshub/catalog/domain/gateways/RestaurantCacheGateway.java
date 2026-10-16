package com.eatshub.catalog.domain.gateways;

import com.eatshub.catalog.domain.model.RestaurantModel;
import reactor.core.publisher.Mono;

public interface RestaurantCacheGateway {

    Mono<RestaurantModel> saveCacheRestaurant(String key, RestaurantModel restaurant);
    Mono<RestaurantModel> getCacheRestaurant(String key);

}
