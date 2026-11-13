package com.eatshub.catalog.infrastructure.entrypoints.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse(

        HttpStatus status,
        String message

) {
}
