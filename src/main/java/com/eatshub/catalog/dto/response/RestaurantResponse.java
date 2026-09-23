package com.eatshub.catalog.dto.response;

import com.eatshub.catalog.enums.PriceRange;
import com.eatshub.catalog.records.Address;
import com.eatshub.catalog.records.ContactInfo;

public record RestaurantResponse(
         String name,
         Address address,
         String cuisineType,
         PriceRange priceRange,
         String openHours,
         String logoUrl,
         String closeAt,
         ContactInfo contactInfo,
         Integer globalRating
) {
}
