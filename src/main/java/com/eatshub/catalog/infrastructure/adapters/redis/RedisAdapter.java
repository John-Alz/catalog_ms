package com.eatshub.catalog.infrastructure.adapters.redis;

import com.eatshub.catalog.domain.gateways.RestaurantCacheGateway;
import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.infrastructure.adapters.redis.dto.RestaurantCacheDto;
import com.eatshub.catalog.infrastructure.adapters.redis.mapper.RestaurantCacheMapper;
import com.mongodb.internal.connection.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisAdapter implements RestaurantCacheGateway {

    private final ReactiveRedisTemplate<String, RestaurantCacheDto> restaurantTemplate;
    private final ReactiveRedisTemplate<String, List<RestaurantCacheDto>> restaurantListTemplate;
    private final RestaurantCacheMapper restaurantCacheMapper;

    private static final Duration TTL = Duration.ofMinutes(2);
    private static final String REDIS_CACHE_PREFIX = "REDIS_CACHE_";


    @Override
    public Mono<RestaurantModel> saveCacheRestaurant(String key, RestaurantModel restaurant) {
        RestaurantCacheDto restaurantCacheDto = restaurantCacheMapper.toDto(restaurant);
        return restaurantTemplate.opsForValue()
                .set(buildNameKey(restaurant.getName()), restaurantCacheDto, TTL)
                .thenReturn(restaurant)
                .doOnSuccess(restaurantModel ->  log.info("Saved restaurant with key {}", restaurantModel.getName()));
    }

    @Override
    public Mono<RestaurantModel> getCacheRestaurant(String key) {
        return restaurantTemplate.opsForValue().get(REDIS_CACHE_PREFIX + key)
                .doOnNext(restaurantCacheDto -> log.info("Get cached restaurant: " + restaurantCacheDto.name()))
                .map(restaurantCacheMapper::toModel);
    }

    @Override
    public Flux<RestaurantModel> getCacheRestaurants(String key) {
        return restaurantListTemplate.opsForValue()
                .get(REDIS_CACHE_PREFIX + key)
                .flatMapMany(Flux::fromIterable)
                .doOnNext(response -> log.info("Get cached restaurants: " + response.name()))
                .map(restaurantCacheMapper::toModel);
    }

    @Override
    public Flux<RestaurantModel> saveCacheRestaurants(String key, List<RestaurantModel> restaurants) {
        return Flux.fromIterable(restaurants)
                .map(restaurantCacheMapper::toDto)
                .collectList()
                .flatMap(restaurantCacheDto -> restaurantListTemplate
                        .opsForValue()
                        .set(REDIS_CACHE_PREFIX + key, restaurantCacheDto, TTL)
                        .thenReturn(restaurants))
                .flatMapMany(Flux::fromIterable)
                .doOnComplete(() -> log.info("Saved restaurants: " + restaurants));
    }


    public static String buildNameKey(String restaurantName) {
        return REDIS_CACHE_PREFIX + restaurantName.toLowerCase();
    }

}
