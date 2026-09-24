package com.eatshub.catalog.infrastructure.adapters.mongodb.validators;

import com.eatshub.catalog.infrastructure.adapters.clients.PlannerMSClient;
import com.eatshub.catalog.domain.exceptions.BusinessException;
import com.eatshub.catalog.domain.model.ReservationModel;
import com.eatshub.catalog.domain.model.RestaurantModel;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.ReservationCollection;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.RestaurantCollection;
import com.eatshub.catalog.infrastructure.adapters.mongodb.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationValidator {

    private final RestaurantRepository restaurantRepository;
    private final PlannerMSClient plannerMSClient;

    public <T>Mono<Void>  applyValidations(T input, List<BusinessValidator<T>> validations) {
        if (validations.isEmpty()) return Mono.empty();
        return validations.stream()
                .reduce(
                        Mono.empty(), (chain, validator) -> chain.then(validator.validate(input)),
                        Mono::then
        );
    }

    public BusinessValidator<ReservationCollection> validateRestaurantNotClosed() {
        log.info("Validating restaurant not closed");
        return reservation ->
             restaurantRepository.findById(reservation.getRestaurantId())
                    .switchIfEmpty(Mono.error(new BusinessException("Restaurant not found")))
                    .flatMap(restaurant -> {
                        if (isRestaurantClosed(restaurant, reservation.getTime())) {
                            return Mono.error(new BusinessException("Restaurant is closed"));
                        }
                        return Mono.empty();
                    });
    }

    public BusinessValidator<ReservationCollection> validateAvailability() {
        log.info("Validating availability");
        return reservation ->
                plannerMSClient.verifyAvailability(reservation.getDate(), reservation.getTime(), reservation.getRestaurantId())
                    .flatMap(isAvailable -> {
                        if (!isAvailable) {
                            return Mono.error(new BusinessException("Availability is not available"));
                        }
                        return Mono.empty();
                    });
    }

    private boolean isRestaurantClosed(RestaurantCollection restaurant, String reservationTime) {
        try {
            if (Objects.isNull(restaurant.getCloseAt()) || Objects.isNull(reservationTime)) {
                return true;
            }
            LocalTime closeLocalTime = LocalTime.parse(restaurant.getCloseAt(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime reservtionLocalTime = LocalTime.parse(reservationTime, DateTimeFormatter.ofPattern("HH:mm"));

            return reservtionLocalTime.isAfter(closeLocalTime);

        } catch (Exception e) {
            log.error("Error on verify close time: " + e);
            return true;
        }
    }

}
