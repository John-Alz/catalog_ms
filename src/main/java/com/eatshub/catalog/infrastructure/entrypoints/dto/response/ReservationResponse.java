package com.eatshub.catalog.infrastructure.entrypoints.dto.response;

import lombok.Builder;

@Builder
public record ReservationResponse(
        String restaurantId,
        String customerName,
        String dateTime,
        String partySize
) {
}
