package com.eatshub.catalog.domain.exceptions;

import com.eatshub.catalog.domain.enums.TechnicalMessage;

public class TechnicalException extends RuntimeException {

    public TechnicalException(String message, TechnicalMessage technicalMessage) {
        super(message);
    }

}
