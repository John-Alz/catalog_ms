package com.eatshub.catalog.infrastructure.adapters.mongodb.repositories;

import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.RestaurantCollection;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface RestaurantRepository extends ReactiveMongoRepository<RestaurantCollection, UUID> {

    Flux<RestaurantCollection> findByCuisineType(String cuisineType);
    Flux<RestaurantCollection> findByNameContainingIgnoreCase(String name);
    Flux<RestaurantCollection> findByPriceType(PriceType priceType);
    Flux<RestaurantCollection> findByAddressCity(String city);

}
