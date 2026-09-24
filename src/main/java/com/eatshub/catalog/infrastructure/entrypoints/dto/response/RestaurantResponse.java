package com.eatshub.catalog.infrastructure.entrypoints.dto.response;

import com.eatshub.catalog.domain.enums.PriceRange;
import com.eatshub.catalog.domain.records.Address;
import com.eatshub.catalog.domain.records.ContactInfo;

public record RestaurantResponse(
         String name,
         Address address,
         String cuisineType,
         PriceRange priceRange,
         String openHours,
         String logoUrl,
         String closeAt,
         ContactInfo contactInfo,
         Double globalRating
) {
}
