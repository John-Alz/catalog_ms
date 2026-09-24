package com.eatshub.catalog.domain.gateways;

import com.eatshub.catalog.domain.enums.PriceRange;
import com.eatshub.catalog.domain.model.RestaurantModel;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RestaurantCatalogGateway {

    Flux<RestaurantModel> readAll();
    Flux<RestaurantModel> readByCuisineType(String cuisineType);
    Flux<RestaurantModel> readByName(String name);
    Flux<RestaurantModel> readByPriceRange(List<PriceRange> priceRanges);
    Flux<RestaurantModel> readByCity(String city);

}
