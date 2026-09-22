package com.eatshub.catalog.services.impls;

import com.eatshub.catalog.enums.PriceRange;
import com.eatshub.catalog.model.ReservationCollection;
import com.eatshub.catalog.model.RestaurantCollection;
import com.eatshub.catalog.records.Address;
import com.eatshub.catalog.repositories.RestaurantRepository;
import com.eatshub.catalog.services.definitions.RestaurantCatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantCatalogServiceImpl implements RestaurantCatalogService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Flux<RestaurantCollection> readAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Flux<RestaurantCollection> readByCuisineType(String cuisineType) {
        return restaurantRepository.findByCuisineType(cuisineType)
                .doOnSubscribe(subscription -> log.info("Cuisine type: " + cuisineType))
                .doOnNext(restaurant -> log.info("Found with param: " + restaurant.getName()))
                .onErrorResume(error -> {
                    log.error("Error: " + error.getMessage());
                    return  Flux.empty();
                });
    }

    @Override
    public Flux<RestaurantCollection> readByByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name)
                .doOnSubscribe(subscription -> log.info("Name restaurant: " + name))
                .doOnNext(restaurant -> log.info("Found with param: " + restaurant.getName()))
                .doOnError(error -> log.error("Error: " + error.getMessage()))
                .onErrorResume(error -> Flux.empty());
    }

    @Override
    public Flux<RestaurantCollection> readByPriceRange(List<PriceRange> priceRanges) {
        return restaurantRepository.findByPriceRangeIn(priceRanges)
                .switchIfEmpty(Flux.empty().cast(RestaurantCollection.class)
                        .doOnSubscribe(subscription -> log.info("Restaurants is empty")));
    }

    @Override
    public Flux<RestaurantCollection> readByCity(String city) {
        return validateCityExists(city)
                .thenMany(restaurantRepository.findByAddressCity(city))
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
