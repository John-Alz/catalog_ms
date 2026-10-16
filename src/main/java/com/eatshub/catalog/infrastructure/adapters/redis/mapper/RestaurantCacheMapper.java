package com.eatshub.catalog.infrastructure.adapters.redis.mapper;

import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.infrastructure.adapters.redis.dto.RestaurantCacheDto;
import org.mapstruct.factory.Mappers;

public interface RestaurantCacheMapper {

    RestaurantModel toModel(RestaurantCacheDto restaurantCacheDto);
    RestaurantCacheDto toDto(RestaurantModel restaurant);
}
