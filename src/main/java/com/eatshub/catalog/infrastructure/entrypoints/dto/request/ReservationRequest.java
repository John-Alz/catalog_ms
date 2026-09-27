package com.eatshub.catalog.infrastructure.entrypoints.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReservationRequest(

         @NotNull(message = "restaurant ID is required")
         @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "restaurant ID must be a valid UUID")
         String restaurantId,

         @NotNull(message = "Customer ID is required")
         @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "Customer ID must be a valid UUID")
         String customerId,

         @NotNull(message = "Customer Name is required")
         @Size(min = 1, max = 21, message = "Customer name must be between 3 and 21 characters")
         String customerName,

         @NotNull(message = "Customer Email is required")
         @Email(message = "Customer Email must be valid")
         String customerEmail,

         @NotNull(message = "Date Time is required")
         @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2},\\d{2}:\\d{2}$",
                message = "DateTime must be in format YYYY-MM-DD,HH:mm (example: 2025-06-16,15:30)")
         String dateTime,

         @NotNull
         @Min(value = 1, message = "Party size must be at least 1")
         @Max(value = 20, message = "Party size cannot exceed 20")
         Integer partySize,
         String comment
) {
}
