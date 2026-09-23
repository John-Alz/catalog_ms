package com.eatshub.catalog.dto.response;

import lombok.Builder;

@Builder
public record ReservationResponse(
        String restaurantId,
        String customerName,
        String dateTime,
        String partySize
) {
}
