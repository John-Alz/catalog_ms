package com.eatshub.catalog.domain.usecase;

import com.eatshub.catalog.domain.constats.Constants;
import com.eatshub.catalog.domain.exceptions.BusinessException;
import com.eatshub.catalog.domain.gateways.RestaurantCacheGateway;
import com.eatshub.catalog.domain.gateways.RestaurantCatalogGateway;
import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.domain.records.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class RestaurantUseCase {

    private final RestaurantCatalogGateway restaurantCatalogGateway;
    private final RestaurantCacheGateway restaurantCacheGateway;

    public Flux<RestaurantModel> readAll(Long page, Long pageSize) {
        return restaurantCatalogGateway.readAll(page, pageSize);
    }

    public Flux<RestaurantModel> readByCuisineType(String cuisineType) {
        final String cacheKey = buildNameKey(cuisineType);
        return restaurantCacheGateway.getCacheRestaurants(cacheKey)
                .switchIfEmpty(Flux.defer(() -> restaurantCatalogGateway.readByCuisineType(cuisineType)
                        .collectList()
                        .flatMapMany(restaurant -> restaurantCacheGateway.saveCacheRestaurants(cacheKey, restaurant))
                ));
    }

    public Mono<RestaurantModel> readByName(String name) {
        final String cacheKey = buildNameKey(name);
        return restaurantCacheGateway.getCacheRestaurant(cacheKey)
                .switchIfEmpty(Mono.defer(() -> restaurantCatalogGateway.readByName(name)
                        .flatMap(restaurant -> restaurantCacheGateway.saveCacheRestaurant(cacheKey, restaurant))
                ));
    }

    public Flux<RestaurantModel> readByPriceRange(String priceType) {
        return restaurantCatalogGateway.readByPriceType(PriceType.valueOf(priceType.toUpperCase()));
    }
    public Flux<RestaurantModel> readByCity(String city) {
        final String cacheKey = buildNameKey(city);
        return  validateCityExists(city)
                .flatMapMany(item -> restaurantCacheGateway.getCacheRestaurants(cacheKey)
                        .switchIfEmpty(Flux.defer(() -> restaurantCatalogGateway.readByCity(city)
                                .collectList()
                                .flatMapMany(restaurantModelList -> restaurantCacheGateway.saveCacheRestaurants(cacheKey, restaurantModelList))
                        )));
    }

    private Mono<Boolean> validateCityExists(String city){
        return restaurantCatalogGateway.existByCity(city)
                .flatMap(isExist -> {
                    if (!isExist) {
                        return Mono.error(new BusinessException("City not found: " + city));
                    }
                    return Mono.just(true);
                });
    }

    private static String buildNameKey(String restaurantName) {
        return Constants.REDIS_CACHE_PREFIX + restaurantName.toLowerCase();
    }

}
