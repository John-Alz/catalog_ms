package com.eatshub.catalog.mapper;

import com.eatshub.catalog.dto.request.ReservationRequest;
import com.eatshub.catalog.dto.response.ReservationResponse;
import com.eatshub.catalog.model.ReservationCollection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "dateTime", expression = "java(joinDateAndTime(reservation.getDate(), reservation,getTime()))")
    @Mapping(target = "notes", source = "comment")
    ReservationCollection toModel(ReservationRequest reservationRequest);

    @Mapping(target = "dateTime", expression = "java(joinDateAndTime(reservation.getDate(), reservation,getTime()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "time", ignore = true)
    ReservationResponse toResponse(ReservationCollection reservation);

    default Mono<ReservationCollection> toRequestMono(Mono<ReservationRequest> reservationRequest) {
        return reservationRequest.map(this::toModel);
    }

    default Mono<ReservationResponse> toResponseMono(Mono<ReservationCollection> reservation) {
        return reservation.map(this::toResponse);
    }

    default Flux<ReservationResponse> toResponseFlux(Flux<ReservationCollection> reservations) {
        return reservations.map(this::toResponse);
    }

    default String joinDateAndTime(String date, String time) {
        return String.join(",", date, time);
    }

    default void splitDateTime(ReservationRequest reservationRequest,
                               @MappingTarget ReservationCollection reservation) {
        if (Objects.nonNull(reservationRequest.dateTime())) {
            String[] dateTime = reservationRequest.dateTime().split(",");
            reservation.setDate(dateTime[0]);
            reservation.setTime(dateTime[1]);
        }
    }


}
