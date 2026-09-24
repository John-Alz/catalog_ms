package com.eatshub.catalog.infrastructure.adapters.mongodb.mapper;

import com.eatshub.catalog.domain.model.ReservationModel;
import com.eatshub.catalog.infrastructure.adapters.mongodb.entity.ReservationCollection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationMapper MAPPER = Mappers.getMapper(ReservationMapper.class);

    ReservationModel toModel(ReservationCollection collection);
    ReservationCollection toEntity(ReservationModel model);

}
