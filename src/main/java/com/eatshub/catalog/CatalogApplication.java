package com.eatshub.catalog;

import com.eatshub.catalog.domain.model.ReservationModel;
import com.eatshub.catalog.infrastructure.adapters.mongodb.repositories.ReservationRepository;
import com.eatshub.catalog.domain.gateways.ReservationGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class CatalogApplication implements CommandLineRunner {

	@Autowired
	private ReservationGateway reservationCrudService;

	@Autowired
	private ReservationRepository reservationRepository;

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {


		final var parrillaModernaID = "0ee619ba-e95f-4103-99f7-ee9cdf831d90";
		final var unavailableID = "dfcbe98d-392b-4b93-9a49-27005223d15d";


//        final var michaelReservation = createTestReservation(
//				parrillaModernaID,
//                "Michael Davis",
//                2,
//                "2025-06-16",
//                "19:00",
//                "Anniversary dinner - romantic table"
//        );
//
//        final var michaelReservationCreated = reservationCrudService.createReservation(michaelReservation)
//        .block();
//
//        System.out.println("michaelReservationCreated: " + michaelReservationCreated.getId());

		final var michaelReservationToUpdate = reservationCrudService.readByReservationId(UUID.fromString("dacac8c4-5f22-46ba-a28b-c1e3a83c6da5")).block();

		michaelReservationToUpdate.setTime("17:30");
		michaelReservationToUpdate.setPartySize(3);

		final var michaelReservationUpdated =
				this.reservationCrudService.updateReservation(michaelReservationToUpdate, UUID.fromString("dacac8c4-5f22-46ba-a28b-c1e3a83c6da5")).block();

		System.out.println("michael reservation updated: " + michaelReservationUpdated.getDate());
		System.out.println("michael reservation updated: " + michaelReservationUpdated.getPartySize());

	}

	private ReservationModel createTestReservation(String restaurantId, String customerName,
												   int partySize, String date, String time, String notes) {
		return ReservationModel.builder()
				.id(UUID.randomUUID())
				.restaurantId(UUID.fromString(restaurantId))
				.customerName(customerName)
				.partySize(partySize)
				.date(date)
				.time(time)
				.notes(notes)
				.build();
	}
}
