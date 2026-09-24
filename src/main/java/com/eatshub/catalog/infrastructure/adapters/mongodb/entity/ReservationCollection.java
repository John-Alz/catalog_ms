package com.eatshub.catalog.infrastructure.adapters.mongodb.entity;

import com.eatshub.catalog.domain.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "reservations")
public class ReservationCollection {

    @Id
    private UUID id;
    @Indexed
    private UUID restaurantId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String date;
    private String time;
    private Integer partySize;
    @Indexed
    private ReservationStatus status;
    private String notes;

}
