package com.eatshub.catalog.repositories;

import com.eatshub.catalog.model.ReservationCollection;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface ReservationRepository extends ReactiveMongoRepository<ReservationCollection, UUID> {

}
