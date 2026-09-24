package com.eatshub.catalog.domain.usecase;

import com.eatshub.catalog.domain.gateways.RestaurantCatalogGateway;
import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.domain.records.Address;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.RestaurantCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class RestaurantUseCase {

    private final RestaurantCatalogGateway restaurantCatalogGateway;

    public Flux<RestaurantModel> readAll() {
        return restaurantCatalogGateway.readAll();
    }

    public Flux<RestaurantModel> readByCuisineType(String cuisineType) {
        return restaurantCatalogGateway.readByCuisineType(cuisineType);
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

}
