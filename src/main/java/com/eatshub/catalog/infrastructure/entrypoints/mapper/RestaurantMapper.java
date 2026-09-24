package com.eatshub.catalog.infrastructure.entrypoints.mapper;

import com.eatshub.catalog.domain.model.Review;
import com.eatshub.catalog.infrastructure.entrypoints.dto.response.RestaurantResponse;
import com.eatshub.catalog.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantMapper MAPPER = Mappers.getMapper(RestaurantMapper.class);

    Double DEFAULT_GLOBAL_RATING = 0.0;

    @Mapping(target = "globalRating", expression = "java(calculateGlobalRating(restaurant.getReviews()))")
    RestaurantResponse toResponse(RestaurantModel restaurant);

    default Mono<RestaurantResponse> toResponseMono(Mono<RestaurantModel> restaurant) {
        return restaurant.map(this::toResponse);
    }

    default Flux<RestaurantResponse> toResponseFLux(Flux<RestaurantModel> restaurants) {
        return restaurants.map(this::toResponse);
    }

    default Double calculateGlobalRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) return DEFAULT_GLOBAL_RATING;
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(DEFAULT_GLOBAL_RATING);
    }
}
