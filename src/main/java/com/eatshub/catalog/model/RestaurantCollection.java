package com.eatshub.catalog.model;

import com.eatshub.catalog.dto.Review;
import com.eatshub.catalog.enums.PriceRange;
import com.eatshub.catalog.records.Address;
import com.eatshub.catalog.records.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "restaurants")
public class RestaurantCollection {

    @Id
    private UUID id;
    @Indexed
    private String name;
    private String capacity;
    private Address address;
    @Indexed
    private String cuisineType;
    @Indexed
    private PriceRange priceRange;
    private String openHours;
    private String logoUrl;
    private String closeAt;
    private ContactInfo contactInfo;
    private List<Review> reviews;


}
