package com.eatshub.catalog.domain.usecase;

import com.eatshub.catalog.domain.gateways.RestaurantCatalogGateway;
import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.domain.enums.PriceType;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
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
        return  restaurantCatalogGateway.readByCity(city);
    }

}
