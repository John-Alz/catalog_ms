package com.eatshub.catalog.infrastructure.entrypoints.dto.response;

public record ErrorResponse(

        Integer status,
        String message

) {
}
