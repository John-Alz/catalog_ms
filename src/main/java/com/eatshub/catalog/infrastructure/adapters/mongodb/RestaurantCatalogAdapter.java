package com.eatshub.catalog.infrastructure.adapters.mongodb;

import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.RestaurantCollection;
import com.eatshub.catalog.infrastructure.adapters.mongodb.mapper.RestaurantAdapterMapper;
import com.eatshub.catalog.infrastructure.adapters.mongodb.repositories.RestaurantRepository;
import com.eatshub.catalog.domain.gateways.RestaurantCatalogGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantCatalogAdapter implements RestaurantCatalogGateway {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Flux<RestaurantModel> readAll(Long page, Long pageSize) {
        return restaurantRepository.findAll()
                .skip(page * pageSize)
                .take(pageSize)
                .doOnNext(restaurantModel -> log.info("id restaurant: " + restaurantModel.getId()))
                .map(RestaurantAdapterMapper.MAPPER::toModel);
    }

    @Override
    public Mono<RestaurantModel> readById(UUID id) {
        return restaurantRepository.findById(id)
                .map(RestaurantAdapterMapper.MAPPER::toModel);
    }

    @Override
    public Flux<RestaurantModel> readByCuisineType(String cuisineType) {
        return restaurantRepository.findByCuisineType(cuisineType)
                .map(RestaurantAdapterMapper.MAPPER::toModel)
                .doOnSubscribe(subscription -> log.info("Cuisine type: " + cuisineType))
                .doOnNext(restaurant -> log.info("Found with param: " + restaurant.getName()))
                .onErrorResume(error -> {
                    log.error("Error: " + error.getMessage());
                    return  Flux.empty();
                });
    }

    @Override
    public Mono<RestaurantModel> readByName(String name) {
        return restaurantRepository.findByNameIgnoreCase(name)
                .map(RestaurantAdapterMapper.MAPPER::toModel)
                .doOnSubscribe(subscription -> log.info("Name restaurant: " + name))
                .doOnNext(restaurant -> log.info("Found with param: " + restaurant.getName()))
                .doOnError(error -> log.error("Error: " + error.getMessage()))
                .onErrorResume(error -> Mono.empty());
    }

    @Override
    public Flux<RestaurantModel> readByPriceType(PriceType priceType) {
        return restaurantRepository.findByPriceType(priceType)
                .switchIfEmpty(Flux.empty().cast(RestaurantCollection.class)
                        .doOnSubscribe(subscription -> log.info("Restaurants is empty")))
                .map(RestaurantAdapterMapper.MAPPER::toModel);
    }

    @Override
    public Flux<RestaurantModel> readByCity(String city) {
        return restaurantRepository.findByAddressCity(city)
                .map(RestaurantAdapterMapper.MAPPER::toModel)
                .doOnNext(restaurant -> log.info("Found restaurant in "+ city + " with name: " + restaurant.getName()))
                .doOnError(error -> log.error("Error: " + error.getMessage()));
    }

    @Override
    public Mono<Boolean> existByCity(String city) {
        return restaurantRepository.existsByAddressCity(city);
    }

}
