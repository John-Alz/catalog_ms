package com.eatshub.catalog.infrastructure.adapters.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private String customerId;
    private String customerName;
    private Integer rating;
    private String comment;
    private LocalDateTime timestamp;

}
