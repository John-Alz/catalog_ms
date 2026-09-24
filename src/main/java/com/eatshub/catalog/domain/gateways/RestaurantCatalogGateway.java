package com.eatshub.catalog.domain.gateways;

import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.domain.model.RestaurantModel;
import reactor.core.publisher.Flux;

public interface RestaurantCatalogGateway {

    Flux<RestaurantModel> readAll();
    Flux<RestaurantModel> readByCuisineType(String cuisineType);
    Flux<RestaurantModel> readByName(String name);
    Flux<RestaurantModel> readByPriceType(PriceType priceType);
    Flux<RestaurantModel> readByCity(String city);

}
