package com.eatshub.catalog.domain.model;

import com.eatshub.catalog.domain.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationModel {

    private UUID id;
    private UUID restaurantId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String date;
    private String time;
    private Integer partySize;
    private ReservationStatus status;
    private String notes;

}
