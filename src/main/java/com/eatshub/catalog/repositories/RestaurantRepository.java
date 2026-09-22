package com.eatshub.catalog.repositories;

import com.eatshub.catalog.enums.PriceRange;
import com.eatshub.catalog.model.RestaurantCollection;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends ReactiveMongoRepository<RestaurantCollection, UUID> {

    Flux<RestaurantCollection> findByCuisineType(String cuisineType);
    Flux<RestaurantCollection> findByNameContainingIgnoreCase(String name);
    Flux<RestaurantCollection> findByPriceRangeIn(List<PriceRange> price);
    Flux<RestaurantCollection> findByAddressCity(String city);

}
