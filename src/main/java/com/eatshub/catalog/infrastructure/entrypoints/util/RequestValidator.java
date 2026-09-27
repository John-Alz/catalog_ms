package com.eatshub.catalog.infrastructure.entrypoints.util;

import com.eatshub.catalog.domain.enums.TechnicalMessage;
import com.eatshub.catalog.domain.exceptions.BusinessException;
import com.eatshub.catalog.domain.exceptions.TechnicalException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class RequestValidator {

    private static final String OPERATION = "Request Validator";
    private static final String ERROR_KEY = "error";

    private final Validator validator;

    public <T> Mono<T> validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> String.format("%s: %s",
                            violation.getPropertyPath(),
                            violation.getMessage()))
                    .collect(Collectors.joining(", "));

            log.error("Request Validator Error: ", errorMessage);
            return Mono.error(new TechnicalException(errorMessage, TechnicalMessage.INVALID_REQUEST));
        }

        return Mono.just(object);
    }
}
