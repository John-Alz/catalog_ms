package com.eatshub.catalog.infrastructure.adapters.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlannerMSClient {

    private static final String UNAVAILABLE_RESTAURANT_ID = "dfcbe98d-392b-4b93-9a49-27005223d15d"; //Alaways full

    public Mono<Boolean> verifyAvailability(String date, String time, UUID restaurantId) {
        return Mono.fromCallable(() -> !UNAVAILABLE_RESTAURANT_ID.equals(restaurantId.toString()))
                .delayElement(getRandomDuration())
                .doOnNext(duration -> log.info("Verifying availability for restaurant: %s", restaurantId));
    }

    private Duration getRandomDuration() {
        final var  randomInt = ThreadLocalRandom.current().nextInt(0, 1000);
        return Duration.ofMillis(randomInt);
    }



}
