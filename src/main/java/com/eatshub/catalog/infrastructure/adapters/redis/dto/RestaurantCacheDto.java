package com.eatshub.catalog.infrastructure.adapters.redis.dto;

import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.domain.model.Review;
import com.eatshub.catalog.domain.records.Address;
import com.eatshub.catalog.domain.records.ContactInfo;

import java.util.List;
import java.util.UUID;

public record RestaurantCacheDto(

         UUID id,
         String name,
         String capacity,
         Address address,
         String cuisineType,
         PriceType priceType,
         String openHours,
         String logoUrl,
         String closeAt,
         ContactInfo contactInfo,
         List<Review>reviews


) {
}
