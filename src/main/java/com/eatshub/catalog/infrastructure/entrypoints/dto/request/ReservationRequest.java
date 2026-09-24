package com.eatshub.catalog.infrastructure.entrypoints.dto.request;

public record ReservationRequest(
         String restaurantId,
         String customerId,
         String customerName,
         String customerEmail,
         String dateTime, // example 2025-06-16,15:30
         Integer partySize,
         String comment
) {
}
