package com.eatshub.catalog.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalMessage {

    INVALID_REQUEST("400", "Error en los datos de la peticion.");

    private final String code;
    private final String message;

}
