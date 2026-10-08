package com.eatshub.catalog.infrastructure.entrypoints.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponse(

        Integer status,
        String message

) {
}
