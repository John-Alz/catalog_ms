package com.eatshub.catalog.services.definitions;

import com.eatshub.catalog.enums.PriceRange;
import com.eatshub.catalog.model.RestaurantCollection;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RestaurantCatalogService {

    Flux<RestaurantCollection> readAll();
    Flux<RestaurantCollection> readByCuisineType(String cuisineType);
    Flux<RestaurantCollection> readByByName(String name);
    Flux<RestaurantCollection> readByPriceRange(List<PriceRange> priceRanges);
    Flux<RestaurantCollection> readByCity(String city);

}
