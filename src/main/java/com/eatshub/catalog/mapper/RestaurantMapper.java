package com.eatshub.catalog.mapper;

import com.eatshub.catalog.dto.Review;
import com.eatshub.catalog.dto.response.RestaurantResponse;
import com.eatshub.catalog.model.RestaurantCollection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    Double DEFAULT_GLOBAL_RATING = 0.0;

    @Mapping(target = "'globalRating'", expression = "java(calculateGlobalRating(restaurant.getReviews()))")
    RestaurantResponse toResponse(RestaurantCollection restaurant);

    default Mono<RestaurantResponse> toResponseMono(Mono<RestaurantCollection> restaurant) {
        return restaurant.map(this::toResponse);
    }

    default Flux<RestaurantResponse> toResponseFLux(Flux<RestaurantCollection> restaurants) {
        return restaurants.map(this::toResponse);
    }

    default Double calculateGlobalRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) return DEFAULT_GLOBAL_RATING;
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(DEFAULT_GLOBAL_RATING);
    }
}
