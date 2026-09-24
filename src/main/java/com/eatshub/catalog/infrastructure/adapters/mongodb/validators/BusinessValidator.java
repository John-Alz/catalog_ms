package com.eatshub.catalog.infrastructure.adapters.mongodb.validators;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BusinessValidator<T> {

    Mono<Void>  validate(T input);
}
