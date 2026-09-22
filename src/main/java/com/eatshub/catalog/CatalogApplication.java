package com.eatshub.catalog;

import com.eatshub.catalog.enums.PriceRange;
import com.eatshub.catalog.services.definitions.RestaurantCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CatalogApplication implements CommandLineRunner {

	@Autowired
	private RestaurantCatalogService restaurantCatalogService;

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		restaurantCatalogService.readByCity("Bogota")
				.doOnNext(System.out::println)
				.subscribe();
	}
}
