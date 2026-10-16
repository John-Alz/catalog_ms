package com.eatshub.catalog.domain.usecase;

import com.eatshub.catalog.domain.constats.Constants;
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

    public Flux<RestaurantModel> readAll() {
        return restaurantCatalogGateway.readAll();
    }

    public Flux<RestaurantModel> readByCuisineType(String cuisineType) {
        final String cacheKey = buildNameKey(cuisineType);
        return restaurantCacheGateway.getCacheRestaurants(cacheKey)
                .switchIfEmpty(Flux.defer(() -> restaurantCatalogGateway.readByCuisineType(cuisineType)
                        .collectList()
                        .flatMapMany(restaurant -> restaurantCacheGateway.saveCacheRestaurants(cacheKey, restaurant))
                ));
    }

    public Flux<RestaurantModel> readByName(String name) {
        return restaurantCatalogGateway.readByName(name);
    }

    public Flux<RestaurantModel> readByPriceRange(String priceType) {
        return restaurantCatalogGateway.readByPriceType(PriceType.valueOf(priceType.toUpperCase()));
    }
    public Flux<RestaurantModel> readByCity(String city) {
        return  validateCityExists(city)
                .thenMany(restaurantCatalogGateway.readByCity(city));
    }

    private Mono<Void> validateCityExists(String city){
        return restaurantCatalogGateway.readAll()
                .map(RestaurantModel::getAddress)
                .filter(Objects::nonNull)
                .map(Address::city)
                .filter(Objects::nonNull)
                .distinct()
                .any(cityName -> cityName.equals(city))
                .flatMap(exist -> {
                    if (exist) {
                        return Mono.empty();
                    }
                    log.warn("City not found: {}", city);
                    return Mono.error(new RuntimeException("City not found: " + city));
                });
    }

    private static String buildNameKey(String restaurantName) {
        return Constants.REDIS_CACHE_PREFIX + restaurantName.toLowerCase();
    }

}
