package com.eatshub.catalog.infrastructure.entrypoints.mapper;

import com.eatshub.catalog.infrastructure.entrypoints.dto.request.ReservationRequest;
import com.eatshub.catalog.infrastructure.entrypoints.dto.response.ReservationResponse;
import com.eatshub.catalog.domain.model.ReservationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ReservationMapper {


    @Mapping(target = "dateTime", expression = "java(joinDateAndTime(reservation.getDate(), reservation.getTime()))")
    ReservationResponse toResponse(ReservationModel reservation);

    @Mapping(target = "notes", source = "comment")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", source = "dateTime", qualifiedByName = "extractDate")
    @Mapping(target = "time", source = "dateTime", qualifiedByName = "extractTime")
    ReservationModel toModel(ReservationRequest reservationRequest);


    default Mono<ReservationModel> toRequestMono(Mono<ReservationRequest> reservationRequest) {
        return reservationRequest.map(this::toModel);
    }

    default Mono<ReservationResponse> toResponseMono(Mono<ReservationModel> reservation) {
        return reservation.map(this::toResponse);
    }

    default Flux<ReservationResponse> toResponseFlux(Flux<ReservationModel> reservations) {
        return reservations.map(this::toResponse);
    }

    default String joinDateAndTime(String date, String time) {
        return String.join(",", date, time);
    }


    @Named("extractDate")
    default String extractDate(String dateTime) {
        return dateTime == null ? null : dateTime.split(",")[0];
    }

    @Named("extractTime")
    default String extractTime(String dateTime) {
        return dateTime == null ? null : dateTime.split(",")[1];
    }



}
