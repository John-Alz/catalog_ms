package com.eatshub.catalog.infrastructure.adapters.mongodb;

import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.domain.records.Address;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.RestaurantCollection;
import com.eatshub.catalog.infrastructure.adapters.mongodb.mapper.RestaurantAdapterMapper;
import com.eatshub.catalog.infrastructure.adapters.mongodb.repositories.RestaurantRepository;
import com.eatshub.catalog.domain.gateways.RestaurantCatalogGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantCatalogAdapter implements RestaurantCatalogGateway {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Flux<RestaurantModel> readAll() {
        return restaurantRepository.findAll()
                .doOnNext(restaurantModel -> log.info("id restaurant: " + restaurantModel.getId()))
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
    public Flux<RestaurantModel> readByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name)
                .map(RestaurantAdapterMapper.MAPPER::toModel)
                .doOnSubscribe(subscription -> log.info("Name restaurant: " + name))
                .doOnNext(restaurant -> log.info("Found with param: " + restaurant.getName()))
                .doOnError(error -> log.error("Error: " + error.getMessage()))
                .onErrorResume(error -> Flux.empty());
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
        return validateCityExists(city)
                .thenMany(restaurantRepository.findByAddressCity(city))
                .map(RestaurantAdapterMapper.MAPPER::toModel)
                .doOnNext(restaurant -> log.info("Found restaurant in "+ city + " with name: " + restaurant.getName()))
                .doOnError(error -> log.error("Error: " + error.getMessage()));
    }


    private Mono<Void> validateCityExists(String city){
            return restaurantRepository.findAll()
                    .map(RestaurantCollection::getAddress)
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
