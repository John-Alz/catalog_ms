package com.eatshub.catalog.infrastructure.adapters.mongodb.mapper;

import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.RestaurantCollection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RestaurantAdapterMapper {

    RestaurantAdapterMapper MAPPER = Mappers.getMapper(RestaurantAdapterMapper.class);

    RestaurantModel toModel(RestaurantCollection collection);
    RestaurantCollection toEntity(RestaurantModel model);

}
