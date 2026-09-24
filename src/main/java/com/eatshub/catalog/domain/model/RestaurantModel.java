package com.eatshub.catalog.domain.model;

import com.eatshub.catalog.domain.enums.PriceType;
import com.eatshub.catalog.domain.records.Address;
import com.eatshub.catalog.domain.records.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "restaurants")
public class RestaurantModel {

    private UUID id;
    private String name;
    private String capacity;
    private Address address;
    private String cuisineType;
    private PriceType priceType;
    private String openHours;
    private String logoUrl;
    private String closeAt;
    private ContactInfo contactInfo;
    private List<Review> reviews;


}
